FROM node
COPY . /app/
RUN ls -l /app/
WORKDIR "/app"
RUN npm install
RUN npm install @angular/cli -g
EXPOSE 4200/tcp
ENTRYPOINT ng serve
