#!/bin/bash
docker rm -f pulse
docker rmi -f pulse
mvn clean install -pl pulse -am -DskipTests=true
docker-compose --file docker-compose-micro.yml up -d
docker logs --follow pulse

