FROM adoptopenjdk/openjdk11:latest
COPY target/user-1.0.0.jar /opt/spring/
WORKDIR /opt/spring
EXPOSE 8090
CMD ["java", "-jar", "user-1.0.0.jar"]