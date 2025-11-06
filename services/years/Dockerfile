# Image légère Python
FROM python:3.12-slim

# Installer dépendances
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copier le code
COPY app.py .

# Définir variable d'environnement par défaut
ENV TARGET_DIR=/data

# Créer le dossier par défaut
RUN mkdir -p /data

# Exposer le port
EXPOSE 8000

# Lancer le serveur
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
