FROM openjdk:11

WORKDIR /app

ADD "target/gestion-station-ski-3.0.jar" /gestion-station-ski-3.0.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/gestion-station-ski-3.0.jar"]

