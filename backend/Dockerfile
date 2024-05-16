FROM openjdk:17-alpine

EXPOSE 5500

ADD target/backend-0.0.1-SNAPSHOT.jar diplom.jar

ENTRYPOINT  ["java", "-jar", "/diplom.jar"]