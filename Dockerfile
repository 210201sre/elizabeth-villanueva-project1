FROM maven:3.6.3-openjdk-8 as builder

COPY pom.xml pom.xml
COPY src/ src/

RUN mvn clean package

FROM java:8 as runner

EXPOSE 7000

ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD

COPY --from=builder target/project1-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]