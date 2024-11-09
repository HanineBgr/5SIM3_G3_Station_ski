FROM openjdk:11

WORKDIR /app

COPY /var/lib/jenkins/workspace/Station\ Ski/target/*.jar /app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/app.jar"]
