FROM maven:3.9-eclipse-temurin-22 as builder
WORKDIR /build
COPY . .
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:22-jre
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
