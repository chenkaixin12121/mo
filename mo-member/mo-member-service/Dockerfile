FROM eclipse-temurin:17-jre-jammy
LABEL authors="chenkaixin12121"
VOLUME /tmp
COPY target/*.jar mo-member.jar
ENTRYPOINT ["java", "-jar", "/mo-member.jar"]
EXPOSE 8040