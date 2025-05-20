# Use a base image (e.g., a specific Java version)
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/*.jar app.jar

# Expose the port that your application listens on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
