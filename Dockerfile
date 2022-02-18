# FROM tomcat:latest
# ADD target/ATM-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
# EXPOSE 8080
# CMD ["catalina.sh", "run"]
FROM openjdk:11
COPY target/ATM.jar app.jar
CMD ["java", "-jar", "app.jar"]