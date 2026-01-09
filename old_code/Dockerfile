# Usar una imagen base con JDK 11 y Maven
FROM maven:3.9.6-eclipse-temurin-11 AS build

# Establecer un directorio de trabajo
WORKDIR /app

# Copiar archivos de tu proyecto al directorio de trabajo
COPY . /app

# Ejecutar Maven para construir el proyecto
RUN mvn clean package

# Crear una nueva imagen basada en OpenJDK 11
FROM eclipse-temurin:21-jdk-alpine

# Exponer el puerto que utilizará la aplicación
EXPOSE 8081

# Copiar el archivo JAR construido desde la etapa anterior
COPY --from=build /app/target/social-seed-0.0.1-SNAPSHOT.jar /app/social-seed-0.0.1-SNAPSHOT.jar

# Establecer el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/social-seed-0.0.1-SNAPSHOT.jar"]