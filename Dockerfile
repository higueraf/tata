FROM maven:3.8.1-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/tata-test-0.0.1-SNAPSHOT.jar /app/tata-test.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tata-test.jar"]
