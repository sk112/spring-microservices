FROM openjdk:latest
# RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JARFILE=target/*.jar
COPY ${JARFILE} app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]