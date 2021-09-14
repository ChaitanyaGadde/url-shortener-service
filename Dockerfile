FROM openjdk:11-jre-slim

LABEL maintainer="Divya Chaitanya Gadde <gaddechaitu2323@gmail.com>"

WORKDIR /opt

ADD  target/*.jar libs/

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xms512m -Xmx3000m"

ENTRYPOINT ["sh", "-c", "java -jar /opt/libs/url-shortener-service*.jar"]

EXPOSE 8080
