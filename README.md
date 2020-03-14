# ourapp

[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://son-api.herokuapp.com/swagger-ui.html#/)
[![Coverage Status](https://coveralls.io/repos/github/codecentric/springboot-sample-app/badge.svg?branch=master)](https://github.com/sonthh/ourapp)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Our [Spring Boot](http://projects.spring.io/spring-boot/) project for backend API

## Technologies used

Spring framework and other libraries:

- Java 8
- Maven 3
- Springboot 2.2.4.RELEASE (Data JPA, Security)
- Netty-SocketIO
- Firebase FCM
- Freemarker for email template
- Thymeleaf template for the testing page with firebase push notification 
- Lombok
- Junit 5
- Swagger 2
- io.jsonwebtoken
- MySQL

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.son.OurAppApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Changing development environment
switch development environment by removing the comment at application.properties file
```shell
// dev or another invironment
# spring.profiles.active=dev
```

## Deploying the application to Heroku

The easiest way to deploy the sample application to Heroku is to use the [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli):

```shell
$ heroku login
$ ...please refer the heroku documentation
```

## CircleCI
CI pipeline testing with [CircleCI](https://github.com/marketplace/circleci)

## Copyright

tranhuuhongson@gmail.com
