FROM gradle:8.14-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle :checkout-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /workspace/checkout-service/build/libs/checkout-service-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]