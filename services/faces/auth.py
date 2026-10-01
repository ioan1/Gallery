import os
import jwt
from jwt import PyJWKClient
from fastapi import HTTPException, Header
from typing import Optional

AWS_REGION = os.getenv("AWS_REGION", "us-east-1")
USER_POOL_ID = os.getenv("USER_POOL_ID", "us-east-1_bHxrl8GNt")
COGNITO_ISSUER = f"https://cognito-idp.{AWS_REGION}.amazonaws.com/{USER_POOL_ID}"
COGNITO_JWKS_URL = f"{COGNITO_ISSUER}/.well-known/jwks.json"

jwks_client = PyJWKClient(COGNITO_JWKS_URL)


def verify_token(authorization: Optional[str] = Header(None)) -> dict:
    if not authorization:
        raise HTTPException(status_code=401, detail="Missing Authorization header")

    if not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Invalid Authorization header format")

    token = authorization.replace("Bearer ", "")

    try:
        signing_key = jwks_client.get_signing_key_from_jwt(token)
        claims = jwt.decode(
            token,
            signing_key.key,
            algorithms=["RS256"],
            issuer=COGNITO_ISSUER,
            options={"verify_exp": True},
        )
        return claims
    except jwt.ExpiredSignatureError:
        raise HTTPException(status_code=401, detail="Token has expired")
    except jwt.InvalidTokenError as exc:
        raise HTTPException(status_code=401, detail=f"Invalid token: {str(exc)}")
    except Exception as exc:
        raise HTTPException(status_code=401, detail=f"Token verification failed: {str(exc)}")
