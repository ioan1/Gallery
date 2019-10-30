FROM alpine:latest
RUN mkdir /app
WORKDIR "/app"
RUN apk add --update nodejs nodejs-npm
RUN npm install --verbose
RUN npm install @angular/cli -g --verbose
EXPOSE 4200/tcp
COPY . /app/
RUN ls -l /app/
ENTRYPOINT ng serve --host 0.0.0.0 --disableHostCheck
