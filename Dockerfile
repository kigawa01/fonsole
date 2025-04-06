FROM gradle:jdk21 AS builder

WORKDIR /app
COPY ./ ./
RUN gradle shadowJar


FROM openjdk:21-bookworm AS runner
WORKDIR /app
COPY --from=builder /app/build/libs/fonsole-all.jar /app
ENTRYPOINT ["exec","java","-jar", "/app/fonsole-all.jar"]