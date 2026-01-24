import os
import jwt
from jwt import PyJWKClient
from fastapi import HTTPException, Header
from typing import Optional

# Configuration AWS Cognito
AWS_REGION = os.getenv("AWS_REGION", "us-east-1")
USER_POOL_ID = os.getenv("USER_POOL_ID", "us-east-1_bHxrl8GNt")
COGNITO_ISSUER = f"https://cognito-idp.{AWS_REGION}.amazonaws.com/{USER_POOL_ID}"
COGNITO_JWKS_URL = f"{COGNITO_ISSUER}/.well-known/jwks.json"

# Client pour récupérer les clés publiques de Cognito
jwks_client = PyJWKClient(COGNITO_JWKS_URL)

def verify_token(authorization: Optional[str] = Header(None)) -> dict:
    """
    Vérifie le token JWT d'AWS Cognito.
    Retourne les claims du token si valide.
    Lève une HTTPException 401 si invalide ou absent.
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="Missing Authorization header")
    
    if not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Invalid Authorization header format")
    
    token = authorization.replace("Bearer ", "")
    
    try:
        # Récupère la clé de signature
        signing_key = jwks_client.get_signing_key_from_jwt(token)
        
        # Décode et vérifie le token
        claims = jwt.decode(
            token,
            signing_key.key,
            algorithms=["RS256"],
            issuer=COGNITO_ISSUER,
            options={"verify_exp": True}
        )
        
        return claims
        
    except jwt.ExpiredSignatureError:
        raise HTTPException(status_code=401, detail="Token has expired")
    except jwt.InvalidTokenError as e:
        raise HTTPException(status_code=401, detail=f"Invalid token: {str(e)}")
    except Exception as e:
        raise HTTPException(status_code=401, detail=f"Token verification failed: {str(e)}")
