# Thumbnail warmer

This Kubernetes CronJob calls the years, albums, and album-content APIs, then requests a thumbnail for each media file supported by the thumbnails service (`jpg`, `jpeg`, `heic`, `mp4`, and `mov`). Requests are sequential, with a one-second pause between files by default.

Build and push the image:

```sh
docker build -t docker.redby.fr/gallery/thumbnail-warmer:1.0.0 ./services/thumbnail-warmer
docker push docker.redby.fr/gallery/thumbnail-warmer:1.0.0
```

Create a dedicated Cognito user named `thumbnails-warmer` with a permanent password. Enable the `USER_PASSWORD_AUTH` flow (also called `ALLOW_USER_PASSWORD_AUTH`) for the app client. Do not use a temporary password that requires an interactive password change at first sign-in.

Create the Kubernetes Secret. The username defaults to `thumbnails-warmer`; enter the password at the hidden prompt so it is not written into shell history or this repository:

```sh
umask 077
secret_file="$(mktemp)"
trap 'rm -f "$secret_file"; unset COGNITO_PASSWORD' EXIT
read -r -p "Cognito username [thumbnails-warmer]: " COGNITO_USERNAME
COGNITO_USERNAME="${COGNITO_USERNAME:-thumbnails-warmer}"
read -r -s -p "Cognito password: " COGNITO_PASSWORD
printf '\n'
printf 'username=%s\npassword=%s\n' "$COGNITO_USERNAME" "$COGNITO_PASSWORD" > "$secret_file"
kubectl create secret generic thumbnail-warmer-auth \
  --namespace gallery \
  --from-env-file="$secret_file" \
  --dry-run=client -o yaml | kubectl apply -f -
```

The job exchanges these credentials for a short-lived access token before calling the APIs. The client ID and region in the CronJob must match the Cognito app client used by the frontend. Rotate the password in Cognito and update the Kubernetes Secret together when changing it.

Deploy or update the nightly job:

```sh
kubectl apply -f infra/services/thumbnail-warmer/cronjob.yaml
```

The schedule is 03:15 in the Kubernetes cluster's timezone. `WARM_INTERVAL_SECONDS` can be changed in the CronJob manifest to adjust the pause. To run it immediately, create a one-off Job with `kubectl create job --from=cronjob/thumbnail-warmer thumbnail-warmer-manual -n gallery`.