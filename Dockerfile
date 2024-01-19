# Use the existing Python image as the base
FROM python:3.11.2-slim AS python-base

FROM openjdk:23-jdk-slim

# ejecutar ./mvnw clean package -DskipTests.. saltar los test porque no hay conexion
# copiar el .jar en la raiz de nuestra imagen  
COPY Backend/target/social-seed-0.0.1-SNAPSHOT.jar java-app.jar

# Copy the Python script and make it executable
COPY fake_graph /app
WORKDIR /app

# Copy Python from the python-base image to /usr/local
COPY --from=python-base /usr/local /usr/local

# Install Python dependencies
RUN apt-get update -y
RUN apt-get install -y python3-dev build-essential
RUN apt-get update -y --fix-missing

COPY fake_graph/requirements.txt requirements.txt

RUN pip install -r requirements.txt

EXPOSE 8081
# ejectuta el .jar a traves de java cuando el contenedor levante
ENTRYPOINT ["java", "-jar", "java-app.jar"]