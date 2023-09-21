FROM eclipse-temurin:17-jre-alpine
COPY build/libs/mortgage-*-all.jar mortgage.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "mortgage.jar"]
