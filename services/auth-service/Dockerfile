# Dockerfile en services/auth-service/
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copiar POMs y código
COPY pom.xml ./pom.xml
COPY platform ./platform

# 1. Instalar el POM raíz (necesario para los sub-módulos)
RUN mvn install -N

# 2. Instalar todo el módulo platform y sus starters
RUN mvn -f platform/pom.xml install -DskipTests

# 3. Copiar y compilar el servicio
COPY services/auth-service ./services/auth-service
RUN mvn -f services/auth-service/pom.xml package -DskipTests

# Imagen final
FROM eclipse-temurin:21-jdk AS runner
WORKDIR /app
COPY --from=build /build/services/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","app.jar"]