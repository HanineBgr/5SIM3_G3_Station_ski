FROM alpine
RUN apk add --no-cache openjdk11
COPY target/gestion-station-ski-2.0.jar /app.jar
EXPOSE 8089
CMD ["java", "-jar", "/app.jar"]
