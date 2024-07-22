FROM  openjdk:17-jre
COPY target/pidev*.jar /pidev.jar
CMD ["java", "-jar", "/pidev.jar"]