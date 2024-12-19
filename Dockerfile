# Use an official Gradle image to build the application
FROM gradle:7.6.0-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and configuration files first
COPY gradlew ./
COPY gradle gradle
COPY build.gradle ./
COPY settings.gradle ./

# Copy the application source code
COPY src ./src

# Build the application
RUN ./gradlew build -x test

# Use a lightweight JDK image to run the application
FROM eclipse-temurin:17-jre

# Set the working directory
WORKDIR /app

# Copy the built application JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Build and push the docker image
# docker buildx build --platform linux/arm64 -t jonasblum/sopra-backend:latest --push .
