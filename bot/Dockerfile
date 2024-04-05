FROM openjdk:17.0.2-jdk-slim-buster
ARG JAR_FILE=./bot/target/bot.jar.original
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]