# First stage: Build the JAR file using Maven and OpenJDK 21
FROM openjdk:21-jdk-slim AS build

# Install Maven manually as part of the build process
RUN apt-get update && apt-get install -y maven

WORKDIR /app

# Copy the Maven project files into the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Second stage: Create a minimal runtime image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/payments_back-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]