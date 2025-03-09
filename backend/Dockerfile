FROM maven:3.9.9-amazoncorretto-23 AS build

COPY . /app
WORKDIR /app

RUN mvn clean install -DskipTests

FROM openjdk:23

COPY --from=build /app/REST/target/*.war app.war

EXPOSE 8080

CMD ["java", "-jar", "app.war"]