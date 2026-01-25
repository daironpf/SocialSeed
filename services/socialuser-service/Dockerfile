# Dockerfile en services/socialuser-service/
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copiar POMs y código
COPY pom.xml ./pom.xml
COPY platform ./platform

# 1. Instalar el POM raíz
RUN mvn install -N

# 2. Instalar todo el módulo platform y sus starters
RUN mvn -f platform/pom.xml install -DskipTests

# 3. Copiar y compilar el servicio
COPY services/socialuser-service ./services/socialuser-service
RUN mvn -f services/socialuser-service/pom.xml package -DskipTests

# Imagen final
FROM eclipse-temurin:21-jdk AS runner
WORKDIR /app
COPY --from=builder /build/services/socialuser-service/target/socialuser-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","app.jar"]