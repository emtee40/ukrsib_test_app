#!/bin/bash
apk add --no-cache openjdk8

file="./src/main/resources/application.properties"

version=$(grep -i 'info.build.version' $file  | cut -f2 -d'=')

echo Version of Application: $version

./gradlew build
java -jar ./build/libs/$version.jar