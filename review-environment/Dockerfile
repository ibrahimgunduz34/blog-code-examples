ARG MAVEN_VERSION=3.9-eclipse-temurin-21
ARG JRE_VERSION=21-jre

FROM maven:${MAVEN_VERSION} AS build

ARG CI_COMMIT_REF_NAME=dev

WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q package -DskipTests -Drevision=${CI_COMMIT_REF_NAME}

FROM eclipse-temurin:${JRE_VERSION}

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]

