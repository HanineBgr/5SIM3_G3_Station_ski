FROM openjdk:17-slim
EXPOSE 8089
ADD target/gestion-station-ski-2.0.jar station-ski.jar
ENTRYPOINT ["java", "-jar", "/station-ski.jar"]

