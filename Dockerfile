FROM openjdk:11

WORKDIR /app

ADD "target/gestion-station-ski-2.0.jar" /getion-station-ski-2.0.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/getion-station-ski-2.0.jar"]

