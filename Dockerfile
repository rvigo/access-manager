FROM maven:3.8.4-openjdk-11-slim as build
WORKDIR /build
COPY . .
RUN mvn -q clean install

FROM openjdk:18-ea-11-jdk-alpine3.15 as runtime
WORKDIR /app
COPY --from=build /build/target/access-manager-0.0.1-SNAPSHOT.jar  app.jar
COPY src/main/resources/application.yml .

ENTRYPOINT ["java", "-jar", "app.jar", "--Dspring.config.location=application.yml"]
HEALTHCHECK --interval=10s --timeout=3s --retries=2 CMD wget --spider http://localhost:8080/actuator/health || exit 1
