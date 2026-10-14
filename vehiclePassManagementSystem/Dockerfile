FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/vpms.war vpms.war
EXPOSE 3031
ENTRYPOINT ["java", "-jar", "vpms.war"]