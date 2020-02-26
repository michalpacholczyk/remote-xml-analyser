FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} remote-xml-analyser-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/remote-xml-analyser-0.0.1-SNAPSHOT.jar"]