# Étape 1 : Builder l'application React
FROM arm32v7/node:20-bullseye AS builder

# Créer le répertoire de travail
WORKDIR /app

# Copier package.json et package-lock.json
COPY package*.json ./

# Installer les dépendances
RUN npm install

# Copier le reste du code
COPY . .

# Build de l'application React
RUN npm run build

# Étape 2 : Serveur Nginx pour servir les fichiers statiques
FROM arm32v7/nginx:stable-bullseye

# Copier les fichiers build dans le dossier Nginx
COPY --from=builder /app/build /usr/share/nginx/html

# Copier le fichier de configuration Nginx personnalisé (optionnel)
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exposer le port 80
EXPOSE 80

# Lancer Nginx en mode foreground
CMD ["nginx", "-g", "daemon off;"]
