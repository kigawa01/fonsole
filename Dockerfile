FROM eclipse-temurin:21-jre-noble AS builder

WORKDIR /app
COPY ./ ./
RUN ./gradlew shadowJar


FROM eclipse-temurin:21-jre-noble AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/fonsole-all.jar /app
ENTRYPOINT ["java","-jar", "/app/fonsole-all.jar"]
