FROM  openjdk:17-jdk
COPY target/pidev*.jar /pidev.jar
CMD ["java", "-jar", "/pidev.jar"]