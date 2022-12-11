#!/bin/bash

cd ../api

docker-compose up --detach & ./mvnw compile quarkus:dev
