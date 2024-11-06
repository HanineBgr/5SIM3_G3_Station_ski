WORKDIR /app

COPY target/gestion-station-ski-2.0.jar /app.jar

EXPOSE 8089

CMD ["java", "-jar", "station-ski.jar"]
