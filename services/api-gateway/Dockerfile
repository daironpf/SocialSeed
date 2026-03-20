#FROM maven:3.9.9-eclipse-temurin-24 AS builder
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml ./pom.xml
COPY platform ./platform

RUN mvn install -N

RUN mvn -f platform/pom.xml install -DskipTests

COPY services/api-gateway ./services/api-gateway
RUN mvn -f services/api-gateway/pom.xml package -DskipTests

#FROM openjdk:24-jdk AS runner
FROM eclipse-temurin:21-jdk AS runner

WORKDIR /app

COPY --from=builder /build/services/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 4004

ENTRYPOINT ["java","-jar","app.jar"]
