# FROM backend/maven AS build

# # Copiar el código fuente
# COPY . /app
# WORKDIR /app
# # WORKDIR /app
# # COPY . .


# # Compilar el proyecto con Maven
# # RUN mvn clean package -DskipTests

# FROM eclipse-temurin:21-jdk-alpine

# # Copiar solo los archivos necesarios del resultado de la compilación Maven
# COPY --from=build /app/target/social-seed-0.0.1-SNAPSHOT.jar java-app.jar


# # # copiar el .jar en la raiz de nuestra imagen  
# # COPY target/social-seed-0.0.1-SNAPSHOT.jar java-app.jar

# EXPOSE 8081
# # ejectuta el .jar a traves de java cuando el contenedor levante
# ENTRYPOINT ["java", "-jar", "java-app.jar"]

FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /app

# Copiar el archivo Maven wrapper
COPY /mvnw .
COPY /.mvn .mvn

# COPY mvnw .
# COPY .mvn .mvn

# Copiar archivos de configuración y definición de dependencias
COPY /pom.xml .
COPY /src src
# COPY pom.xml .
# COPY src src

# Construir la aplicación
# RUN mvn -f /app/pom.xml install
RUN mvn -f /app/pom.xml dependency:go-offline
RUN mvn -f /app/pom.xml dependency:resolve
RUN mvn -f /app/pom.xml install
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
# RUN mvn -f /app/pom.xml install
# RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Etapa de Ejecución
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
# FROM eclipse-temurin:21-jdk-alpine
# VOLUME /tmp

# Directorio de dependencias
ARG DEPENDENCY=/app/target/dependency
# ARG DEPENDENCY=/app/target/dependency

# Copiar las dependencias y archivos JAR
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Nombre del JAR generado por tu proyecto
ARG JAR_FILE=social-seed-0.0.1-SNAPSHOT.jar

EXPOSE 8081
# Punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "${JAR_FILE}"]

# ENTRYPOINT ["java", "-jar", "java-app.jar"]