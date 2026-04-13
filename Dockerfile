FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY target/*.jar Planning_tg_bot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Planning_tg_bot.jar"]