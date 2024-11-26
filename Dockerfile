# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /HealthNet

# Copy the application's JAR file into the container
COPY target/HealthNet-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on (usually 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "HealthNetWeb.jar"]
