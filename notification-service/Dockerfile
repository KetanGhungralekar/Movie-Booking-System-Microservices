FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]
