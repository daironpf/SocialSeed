# ===== STAGE 1: Base Maven con librerías compartidas =====
FROM maven:3.9.9-eclipse-temurin-21 AS socialseed-base
LABEL authors="dairon.perezfrias"

WORKDIR /app

# Copiamos los poms raíz y los módulos compartidos
COPY pom.xml ./pom.xml
COPY socialseed-platform/pom.xml ./socialseed-platform/pom.xml
COPY /platform/socialseed-validation-starter/pom.xml ./socialseed-validation-starter/pom.xml

# Instalamos dependencias offline
RUN mvn dependency:go-offline -B

# Copiamos el código de los módulos compartidos
COPY socialseed-platform ./socialseed-platform
COPY socialseed-validation-starter ./socialseed-validation-starter

# Build e instalación en repositorio Maven local
RUN mvn install -DskipTests
