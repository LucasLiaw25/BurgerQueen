FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests && \
    mv target/*.jar app.jar

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/app.jar .

COPY src/main/resources/authz.pub authz.pub
COPY src/main/resources/authz.pem authz.pem

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:InitialRAMPercentage=75.0", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]