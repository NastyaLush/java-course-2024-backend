FROM openjdk:17.0.2-jdk-slim-buster
ARG JAR_FILE=/home/runner/work/java-course-2024-backend/java-course-2024-backend/bot/target/bot.jar.original
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]