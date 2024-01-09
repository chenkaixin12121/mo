FROM eclipse-temurin:17-jre-jammy
LABEL authors="chenkaixin12121"
VOLUME /tmp
COPY target/*.jar mo-auth.jar
ENTRYPOINT ["java", "-jar", "/mo-auth.jar"]
EXPOSE 8020