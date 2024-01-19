FROM openjdk:23-jdk-slim

# Copiar el código fuente
COPY . /app
WORKDIR /app/backend

# copiar el .jar en la raiz de nuestra imagen  
COPY target/social-seed-0.0.1-SNAPSHOT.jar java-app.jar

EXPOSE 8081
# ejectuta el .jar a traves de java cuando el contenedor levante
ENTRYPOINT ["java", "-jar", "java-app.jar"]