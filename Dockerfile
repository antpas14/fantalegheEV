FROM amazoncorretto:21-alpine
VOLUME /tmp

ADD target/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Dspring.application.name=backend", "-jar", "/app.jar"]
RUN sh -c 'touch /app.jar'
