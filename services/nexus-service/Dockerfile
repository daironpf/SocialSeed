FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# Copy root POM and platform
COPY pom.xml ./pom.xml
COPY platform ./platform

# 1. Install root POM
RUN mvn install -N

# 2. Install all platform starters
RUN mvn -f platform/pom.xml install -DskipTests

# 3. Build the service
COPY services/nexus-service ./services/nexus-service
RUN mvn -f services/nexus-service/pom.xml package -DskipTests

FROM eclipse-temurin:21-jdk AS runner
WORKDIR /app
COPY --from=build /build/services/nexus-service/target/nexus-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
