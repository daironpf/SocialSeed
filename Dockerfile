FROM openjdk:23-jdk-Slim

# ejecutar ./mvnw clean package -DskipTests.. saltar los test porque no hay conexion
# copiar el .jar en la raiz de nuestra imagen  
COPY Backend/target/social-seed-0.0.1-SNAPSHOT.jar java-app.jar

EXPOSE 8081
# ejectuta el .jar a traves de java cuando el contenedor levante
ENTRYPOINT ["java", "-jar", "java-app.jar"]