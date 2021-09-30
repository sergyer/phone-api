FROM openjdk:11-jdk as builder
COPY target/phone-api-0.0.1-SNAPSHOT.jar phone-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/phone-api-0.0.1-SNAPSHOT.jar"]
