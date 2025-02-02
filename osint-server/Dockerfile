FROM openjdk:17-jdk-slim as builder

WORKDIR /app

COPY gradlew gradlew.bat /app/
COPY gradle /app/gradle
COPY build.gradle.kts settings.gradle.kts /app/
COPY src /app/src

RUN chmod +x gradlew

RUN ./gradlew shadowJar

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/server.jar /app/server.jar

EXPOSE 8080

CMD ["java", "-jar", "server.jar"]
