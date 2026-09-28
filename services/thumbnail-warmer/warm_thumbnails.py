import json
import logging
import os
import sys
import time
from pathlib import PurePosixPath
from urllib.error import HTTPError, URLError
from urllib.parse import quote, urlencode
from urllib.request import Request, urlopen


SUPPORTED_EXTENSIONS = {".jpg", ".jpeg", ".heic", ".mp4", ".mov"}
YEARS_API_URL = os.getenv("YEARS_API_URL", "http://service-years:8000/years")
ALBUMS_API_URL = os.getenv("ALBUMS_API_URL", "http://service-albums:8000/albums")
THUMBNAILS_API_URL = os.getenv(
    "THUMBNAILS_API_URL", "http://service-thumbnails:8080/thumbnails/small"
)
COGNITO_REGION = os.getenv("COGNITO_REGION", "us-east-1")
COGNITO_CLIENT_ID = os.getenv("COGNITO_CLIENT_ID", "4cm803bb86anli21j7nf29lvhh")
HTTP_TIMEOUT_SECONDS = float(os.getenv("HTTP_TIMEOUT_SECONDS", "120"))
WARM_INTERVAL_SECONDS = float(os.getenv("WARM_INTERVAL_SECONDS", "1"))

logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s %(message)s")
logger = logging.getLogger("thumbnail-warmer")


def fetch_json(url, token):
    request = Request(url, headers={"Authorization": f"Bearer {token}"})
    with urlopen(request, timeout=HTTP_TIMEOUT_SECONDS) as response:
        return json.load(response)


def get_access_token():
    username = os.getenv("COGNITO_USERNAME")
    password = os.getenv("COGNITO_PASSWORD")
    if not username or not password:
        raise ValueError("COGNITO_USERNAME and COGNITO_PASSWORD are required")

    request = Request(
        f"https://cognito-idp.{COGNITO_REGION}.amazonaws.com/",
        data=json.dumps(
            {
                "AuthFlow": "USER_PASSWORD_AUTH",
                "ClientId": COGNITO_CLIENT_ID,
                "AuthParameters": {"USERNAME": username, "PASSWORD": password},
            }
        ).encode(),
        headers={
            "Content-Type": "application/x-amz-json-1.1",
            "X-Amz-Target": "AWSCognitoIdentityProviderService.InitiateAuth",
        },
    )
    with urlopen(request, timeout=HTTP_TIMEOUT_SECONDS) as response:
        result = json.load(response)
    return result["AuthenticationResult"]["AccessToken"]


def iter_media_files(entries, prefix=""):
    for entry in entries:
        name = entry["name"]
        path = f"{prefix}/{name}" if prefix else name
        if entry["type"] == "dir":
            yield from iter_media_files(entry.get("children", []), path)
        elif entry["type"] == "file":
            if PurePosixPath(name).suffix.lower() in SUPPORTED_EXTENSIONS:
                yield path


def warm_thumbnail(year, album_id, image_path, token):
    url = (
        f"{THUMBNAILS_API_URL.rstrip('/')}/{quote(str(year), safe='')}/"
        f"{quote(str(album_id), safe='')}?{urlencode({'name': image_path})}"
    )
    request = Request(url, headers={"Authorization": f"Bearer {token}"})
    with urlopen(request, timeout=HTTP_TIMEOUT_SECONDS) as response:
        response.read()


def run():
    if HTTP_TIMEOUT_SECONDS <= 0 or WARM_INTERVAL_SECONDS < 0:
        logger.error("HTTP_TIMEOUT_SECONDS must be positive and WARM_INTERVAL_SECONDS non-negative")
        return 1

    try:
        token = get_access_token()
        years = fetch_json(YEARS_API_URL, token)
        warmed = 0
        failures = 0

        for year in years:
            albums_url = f"{ALBUMS_API_URL.rstrip('/')}/{quote(str(year), safe='')}"
            albums = fetch_json(albums_url, token)
            for album in albums:
                content_url = f"{albums_url}/{quote(str(album['id']), safe='')}"
                contents = fetch_json(content_url, token)
                for image_path in iter_media_files(contents):
                    try:
                        warm_thumbnail(year, album["id"], image_path, token)
                        warmed += 1
                    except (HTTPError, URLError, TimeoutError, OSError) as error:
                        failures += 1
                        logger.warning(
                            "Could not warm %s/%s/%s: %s",
                            year,
                            album["id"],
                            image_path,
                            error,
                        )
                    if WARM_INTERVAL_SECONDS:
                        time.sleep(WARM_INTERVAL_SECONDS)

        logger.info("Finished: %d thumbnails requested, %d failures", warmed, failures)
        return 1 if failures else 0
    except (HTTPError, URLError, TimeoutError, OSError, ValueError, KeyError) as error:
        logger.exception("Thumbnail warming stopped: %s", error)
        return 1


if __name__ == "__main__":
    sys.exit(run())