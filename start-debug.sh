#!/bin/bash

./mvnw clean spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5050"

#-Dspring-boot.run.profiles=