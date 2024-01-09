FROM eclipse-temurin:17-jre-jammy
LABEL authors="chenkaixin12121"
VOLUME /tmp
COPY target/*.jar mo-gateway.jar
ENTRYPOINT ["java", "-jar", "/mo-gateway.jar"]
EXPOSE 8010