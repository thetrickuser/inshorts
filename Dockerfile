# Step 1: Build the app
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml and download dependencies first (caches better)
COPY news-api/pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build
COPY news-api/src ./src
RUN mvn clean package -DskipTests

# Step 2: Run the app
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy only the fat jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
