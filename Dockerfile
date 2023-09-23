FROM eclipse-temurin:17-jre-alpine
COPY build/libs/mortgage-*-all.jar mortgage-service.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "mortgage-service.jar"]
