FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
ARG BUILD_ENV
ENV BUILD_ENV=${BUILD_ENV:-prod}
RUN echo "Building for $BUILD_ENV Environment"
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ src/
RUN mvn -f pom.xml clean package -DskipTests -P $BUILD_ENV

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar glitterfin.jar
ENTRYPOINT ["java","-jar","glitterfin.jar"]
