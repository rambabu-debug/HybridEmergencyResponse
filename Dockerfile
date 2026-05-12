# ---- Builder stage ----
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy Gradle wrapper and dependency manifests first for layer caching
COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Pre-fetch dependencies (layer is cached as long as build files don't change)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# Copy the full source and build
COPY . .
RUN chmod +x gradlew && ./gradlew clean build -x check -x test -Pproduction --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy only the built JAR from the builder stage
COPY --from=builder /app/build/libs/app.jar app.jar
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar -Dserver.port=${PORT:-8080} /app/app.jar"]
