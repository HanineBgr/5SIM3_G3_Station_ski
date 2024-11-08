FROM openjdk:11

# Set the working directory
WORKDIR /app

COPY target/*.jar /app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "/app.jar"]
