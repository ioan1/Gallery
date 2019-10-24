FROM node:latest
COPY . /app/
RUN ls -l /app/
WORKDIR "/app"
RUN npm install --verbose
RUN npm install @angular/cli -g --verbose
EXPOSE 4200/tcp
ENTRYPOINT ng serve --host 0.0.0.0 --disableHostCheck
