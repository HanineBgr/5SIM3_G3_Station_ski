FROM openjdk:11

WORKDIR /app

COPY /var/lib/jenkins/workspace/StationSki/target/gestion-station-ski-3.0.jar /app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/app.jar"]
