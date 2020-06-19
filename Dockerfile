FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


#  docker run --name personnel-sql -e MYSQL_ROOT_PASSWORD=personnel -e MYSQL_DATABASE=personnel -e MYSQL_USER=personnel
#  -e MYSQL_PASSWORD=personnel -p 3306:3306 -d mysql:5.6
