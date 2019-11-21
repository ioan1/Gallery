FROM jenkins/jenkins:lts-alpine

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

COPY security.groovy /usr/share/jenkins/ref/init.groovy.d/security.groovy
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY jobs /usr/share/jenkins/ref/jobs

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

EXPOSE 8080/tcp