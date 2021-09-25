FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/*.jar ./app.jar

# application
EXPOSE 8080

CMD exec java -Duser.timezone=UTC -jar app.jar