FROM openjdk:17-slim
EXPOSE 8089
ADD target/station-ski-1.0.jar station-ski-1.0.jar
ENTRYPOINT ["java", "-jar", "/station-ski-1.0.jar"]
