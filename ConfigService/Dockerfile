FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY msaEncryptionKey.jks msaEncryptionKey.jks
COPY build/libs/ConfigService-0.0.1-SNAPSHOT.jar ConfigServer.jar
ENTRYPOINT ["java","-jar","ConfigServer.jar"]
