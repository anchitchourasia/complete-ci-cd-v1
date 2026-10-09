FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/ci-cd-demo.war ci-cd-demo.war
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "ci-cd-demo.war"]