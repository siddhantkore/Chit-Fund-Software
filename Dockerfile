# ===============================
# Stage 1: Build the JAR
# ===============================
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (better build caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the project (skip tests for faster builds)
RUN mvn clean package -DskipTests -B -DskipTests=true

# ===============================
# Stage 2: Run the JAR
# ===============================
FROM eclipse-temurin:21-jre-jammy AS runtime

# Create non-root user for better security
RUN useradd --create-home --shell /bin/bash appuser || true

WORKDIR /app

# Copy jar (artifact) from build stage
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} /app/app.jar

# Copy entrypoint script
COPY docker/entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh && chown -R appuser:appuser /app

## Install minimal runtime dependencies (curl for healthcheck)
USER root
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

USER appuser
EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SPRING_PROFILES_ACTIVE=default

# Simple healthcheck (requires actuator or accessible port)
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
	CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["/app/entrypoint.sh"]
