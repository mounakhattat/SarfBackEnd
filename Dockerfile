FROM  openjdk:17-alpine
COPY target/pidev*.jar /pidev.jar
CMD ["java", "-jar", "/pidev.jar"]