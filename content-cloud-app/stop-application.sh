#!/bin/bash

IMAGE_NAME=content-cloud-client
CONTAINER_NAME=content-cloud-app
TAG=latest

echo 'stoping the container....'
docker stop ${CONTAINER_NAME}
echo 'removing the docker image.....'
docker rmi ${IMAGE_NAME}
echo 'cleaning build...'

./mvnw clean