# Stage 1: Build the application
FROM gradle:8.13-jdk17-alpine AS build
WORKDIR /home/gradle/src

# Copy Gradle wrapper and build files first to leverage Docker caching
COPY --from=project gradle ./gradle
COPY --from=project settings.gradle.kts .

# Copy source code and other project files
COPY --from=modules api ./src/api
COPY --from=modules models ./src/models

# Build the fat JAR (or shadow JAR/boot JAR)
RUN gradle src:api:buildFatJar --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /home/gradle/src/src/api/build/libs/*.jar /app/app.jar

# Expose the application port (e.g., for a web application)
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]