FROM alpine:latest
RUN mkdir /app
WORKDIR "/app"
RUN apk add --update nodejs nodejs-npm
RUN npm install --verbose
RUN npm install --verbose -g @angular-devkit/build-angular
RUN npm install @angular/compiler -g --verbose
RUN npm install @angular/compiler-cli -g --verbose
RUN npm install @angular/cli -g --verbose
RUN npm install @angular/core -g --verbose
RUN npm install -g typescript --verbose
EXPOSE 4200/tcp
COPY . /app/
RUN ls -l /app/
RUN npm install --verbose
ENTRYPOINT ng serve --host 0.0.0.0 --disableHostCheck

