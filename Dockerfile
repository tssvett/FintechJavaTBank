FROM gradle:jdk21-alpine AS dependencies
ENV GRADLE_USER_HOME=/cache
WORKDIR /opt/app
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY logtime/build.gradle.kts logtime/build.gradle.kts
RUN gradle dependencies --no-daemon --stacktrace

FROM gradle:jdk21-alpine AS builder
WORKDIR /opt/app
COPY --from=dependencies /cache /home/gradle/.gradle
COPY --from=dependencies /opt/app /opt/app
COPY logtime/src logtime/src
COPY src src
RUN gradle clean bootJar --no-daemon --stacktrace

FROM eclipse-temurin:21-jdk-alpine AS final
WORKDIR /opt/app
COPY --from=builder /opt/app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
