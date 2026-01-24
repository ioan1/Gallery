# Image légère Python
FROM arm32v7/python:3.12-slim-bullseye

RUN apt-get update && \
    apt-get install -y build-essential zlib1g zlib1g-dev libssl-dev libffi-dev curl && \
    rm -rf /var/lib/apt/lists/*

# Installer dépendances
WORKSPACE /app
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip setuptools wheel
RUN pip install --no-cache-dir -r requirements.txt

# Copier le code
COPY app.py .
COPY auth.py .

# Définir variable d'environnement par défaut
ENV TARGET_DIR=/data

# Créer le dossier par défaut
RUN mkdir -p /data

# Exposer le port
EXPOSE 8000

# Lancer le serveur
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
