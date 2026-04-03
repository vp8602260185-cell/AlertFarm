# --- Stage 1: Build ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# Build and verify the jar exists in the logs
RUN mvn clean package -DskipTests && ls -l target/

# --- Stage 2: Runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Add a non-root user (important for production)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Use a specific pattern to grab the executable JAR and skip the "plain" one
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# This MUST be uncommented to run!
ENTRYPOINT ["java", "-jar", "app.jar"]