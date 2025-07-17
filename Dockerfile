FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
RUN echo "Building App"
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ src/
RUN mvn -f pom.xml clean package -DskipTests

FROM eclipse-temurin:21
RUN groupadd -r glitterfin-group && useradd -r -g glitterfin-group glitterfin-user
WORKDIR /app
COPY --from=build /app/target/*.jar glitterfin.jar
RUN chown -R glitterfin-user:glitterfin-group /app
USER glitterfin-user
ARG BUILD_ENV
ENV BUILD_ENV=${BUILD_ENV}
RUN echo "Running app with profile: $BUILD_ENV"
ENTRYPOINT ["java","-jar","glitterfin.jar", "--spring.profiles.active=${BUILD_ENV}"]
