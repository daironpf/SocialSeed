# Dockerfile en services/auth-service/
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copiar POMs y código
COPY ../../pom.xml ./pom.xml
COPY ../../platform ./platform
COPY ../../platform/socialseed-validation-starter ./platform/socialseed-validation-starter
COPY ../../services/auth-service ./services/auth-service

# ===== 3 Instalar platform + starters en .m2 =====
RUN mvn clean install -DskipTests -pl platform,platform/socialseed-validation-starter -am

# Luego compilar auth-service
RUN mvn clean package -DskipTests -pl services/auth-service

# Imagen final
FROM eclipse-temurin:21-jdk AS runner
WORKDIR /app
COPY --from=build /build/services/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","app.jar"]