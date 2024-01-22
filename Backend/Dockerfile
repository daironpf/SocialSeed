# FROM openjdk:23-jdk-slim
# FROM eclipse-temurin:11-jdk-alpine

FROM backend/maven AS build

# Copiar el código fuente
# COPY . /app
# WORKDIR /app
WORKDIR /app
COPY . .


# Compilar el proyecto con Maven
RUN mvn clean package -DskipTests

# FROM eclipse-temurin:21-jdk-alpine
FROM eclipse-temurin:11-jdk-alpine

# Copiar solo los archivos necesarios del resultado de la compilación Maven
COPY --from=build /app/target/social-seed-0.0.1-SNAPSHOT.jar java-app.jar


# # copiar el .jar en la raiz de nuestra imagen  
# COPY target/social-seed-0.0.1-SNAPSHOT.jar java-app.jar

EXPOSE 8081
# ejectuta el .jar a traves de java cuando el contenedor levante
ENTRYPOINT ["java", "-jar", "java-app.jar"]