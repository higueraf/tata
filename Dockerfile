FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/tata-devop.jar /app/tata-devop.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tata-devop.jar"]
