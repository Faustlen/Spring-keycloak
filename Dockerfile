
FROM openjdk:17.0.1-jdk-slim
VOLUME /tmp
COPY target/task-management-system-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
