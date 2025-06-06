FROM maven:3.9.9-amazoncorretto-23 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -f /app/pom.xml -Dmaven.test.skip=true


FROM amazoncorretto:23-alpine AS jre-build
COPY --from=build /app/target/*.jar app.jar
RUN jar xf app.jar
RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 23  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    app.jar > deps.info
RUN jlink \
    --verbose \
    --add-modules $(cat deps.info) \
    --no-header-files \
    --no-man-pages \
    --output /customjre

FROM alpine:3.18
COPY --from=jre-build /customjre /opt/jre
ENV JAVA_HOME=/opt/jre
ENV PATH="$PATH:$JAVA_HOME/bin"
COPY --from=build /app/target/*.jar app.jar
RUN apk add --no-cache curl
EXPOSE 8761
CMD ["java", "-jar", "app.jar"]