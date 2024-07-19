FROM  openjdk:12-alpine
COPY target/pidev*.jar /pidev.jar
CMD ["java", "-jar", "/pidev.jar"]