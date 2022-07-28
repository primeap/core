FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/core-1.0.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
expose 8080