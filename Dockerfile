FROM openjdk:23-jdk

WORKDIR /Backend/src/main/java/com/social/seed
COPY . /app
RUN SocialSeedApplication.java
CMD ["java", "SocialSeedApplication"]