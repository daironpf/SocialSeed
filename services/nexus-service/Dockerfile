FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# Copy root POM and platform
COPY ../../pom.xml ./pom.xml
COPY ../../platform ./platform

# Copy the service
COPY ../../services/nexus-service ./services/nexus-service

# Install platform dependencies
RUN mvn clean install -DskipTests -pl platform -am

# Build the service
RUN mvn clean package -DskipTests -pl services/nexus-service

FROM eclipse-temurin:21-jdk AS runner
WORKDIR /app
COPY --from=build /build/services/nexus-service/target/nexus-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
