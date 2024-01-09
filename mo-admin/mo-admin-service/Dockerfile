FROM eclipse-temurin:17-jre-jammy
LABEL authors="chenkaixin12121"
VOLUME /tmp
COPY target/*.jar mo-admin.jar
ENTRYPOINT ["java", "-jar", "/mo-admin.jar"]
EXPOSE 8030