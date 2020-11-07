FROM openjdk:8-jdk-alpine

RUN apk update
RUN apk add maven

COPY pom.xml .

RUN mvn clean package -Dmaven.main.skip -Dmaven.test.skip && rm -r target

EXPOSE 9090