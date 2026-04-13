FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY target/*.jar planning_tg_bot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "planning_tg_bot.jar"]