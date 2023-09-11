FROM openjdk:17-ea-jdk-slim

VOLUME /tmp

COPY target/user-service-1.0.jar userService.jar

ENTRYPOINT ["java" , "-jar" , "userService.jar" ]
