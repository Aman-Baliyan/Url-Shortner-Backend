# Stage 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first for better layer caching
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Run the application in a slim JRE image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
# Run as a non-root user for security
#RUN addgroup -S appgroup && adduser -S appuser -G appgroup
#USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]