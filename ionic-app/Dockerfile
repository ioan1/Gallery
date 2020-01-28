# base image
FROM nginx:1.16.0-alpine

# copy artifact build from the 'build environment'
COPY htdocs /usr/share/nginx/html

# expose port 81
EXPOSE 81

# run nginx
CMD ["nginx", "-g", "daemon off;"]
