FROM node
COPY . /app/
RUN ls -l /app/
WORKDIR "/app"
RUN npm install
EXPOSE 4200/tcp
ENTRYPOINT ng serve
