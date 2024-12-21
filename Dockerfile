FROM openjdk:17-jdk
ARG JAR_FILE=target/llama2-1.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
