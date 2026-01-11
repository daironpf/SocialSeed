# Dockerfile en services/socialuser-service/
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copiar POMs y código
COPY ../../pom.xml ./pom.xml
COPY ../../platform ./platform
COPY ../../platform/socialseed-validation-starter ./platform/socialseed-validation-starter
COPY ../../services/socialuser-service ./services/socialuser-service

# ===== 3 Instalar platform + starters en .m2 =====
RUN mvn clean install -DskipTests -pl platform,platform/socialseed-validation-starter -am

# Luego compilar socialuser-service
RUN mvn clean package -DskipTests -pl services/socialuser-service

# Imagen final
FROM eclipse-temurin:21-jdk AS runner
WORKDIR /app
COPY --from=builder /build/services/socialuser-service/target/socialuser-service-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","app.jar"]