#!/bin/sh
file="./src/main/resources/application.properties"

version=$(grep -i 'info.build.version' $file  | cut -f2 -d'=')

echo Version of Json API: $version

./gradlew build -Dspring.profiles.active=docker
java -jar ./build/libs/$version.jar  --spring.profiles.active=docker