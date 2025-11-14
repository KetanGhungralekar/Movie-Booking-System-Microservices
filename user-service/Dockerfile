FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar app.jar
# Activate docker profile
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
