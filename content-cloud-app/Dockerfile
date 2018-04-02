FROM openjdk:8-jre-alpine
ADD target/content-cloud-client.jar app.jar
CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=dev", "/app.jar"]
