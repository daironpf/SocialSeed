FROM maven:3.9.9-eclipse-temurin-24 AS builder

WORKDIR /app

COPY pom.xml .

#instala las dependencias y si no hay cambio no vuelve a descargarla y podemos hacer stop/start sin volver a descargar dependencias
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:24-jdk AS runner


WORKDIR /app

COPY --from=builder ./app/target/api-gateway-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]