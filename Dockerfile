FROM openjdk:17
EXPOSE 8089

WORKDIR /app


RUN curl -u admin:admin -o app.jar "http://192.168.33.10:8081/nexus/content/repositories/releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar"
ENTRYPOINT ["java","-jar","app.jar"]