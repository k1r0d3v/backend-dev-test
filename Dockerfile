FROM maven:3-eclipse-temurin-17-alpine AS MAVEN_BUILD

MAINTAINER Alejandro Romero Rivera

# Setup the build environment
WORKDIR /build/

# Unsets MAVEN_CONFIG the container predefined enviroment variable that collides with maven wrapper
ENV MAVEN_CONFIG=""

# Generate a maven wrapper
COPY pom.xml /build/
RUN mvn wrapper:wrapper -Dmaven=3.8.6

# Download dependeces in this layer
RUN ./mvnw dependency:go-offline

# New layer with source code, this avoids to re-download all dependeces
COPY src /build/src
RUN ./mvnw clean package

# Execution environment
FROM eclipse-temurin:17-alpine
ENV SPRING_ACTIVE_PROFILE="production"
ENV SERVER_PORT=5000
ENV PRODUCTS_REPOSITORY_HOST="localhost"
ENV PRODUCTS_REPOSITORY_PORT="3001"
COPY --from=MAVEN_BUILD /build/target/backend-dev-test-0.0.1-SNAPSHOT.jar /app/

# Expose the server port to allow communication with other containers
EXPOSE ${SERVER_PORT}

# Execute
WORKDIR /app/
ENTRYPOINT ["java", "-jar", "backend-dev-test-0.0.1-SNAPSHOT.jar", "--server.port=${SERVER_PORT}", "--spring.profiles.active=${SPRING_ACTIVE_PROFILE}", "--products.repository.external.host=${PRODUCTS_REPOSITORY_HOST}", "--products.repository.external.port=${PRODUCTS_REPOSITORY_PORT}"]