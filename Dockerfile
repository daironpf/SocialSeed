# Etapa de compilación
FROM maven:3.9.11-eclipse-temurin-24 AS build
WORKDIR /app

# Copiar proyecto
COPY . .

# Compilar
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:24-jre
WORKDIR /app

# Copiar jar compilado
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto del backend
EXPOSE 8081

CMD ["java", "-jar", "app.jar"]
