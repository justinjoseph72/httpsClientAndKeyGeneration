#!/bin/bash
set -euo pipefail

IMAGE_NAME=content-cloud-client
CONTAINER_NAME=content-cloud-app
TAG=latest

echo 'Building Application Code Without Running Tests Or Code Quality Checks...'
./mvnw clean install -DskipTests
echo 'Application Built. Now building docker image...'

docker build -t ${IMAGE_NAME}:${TAG} .

sleep 5
echo 'starting container...'

docker run --rm --name ${CONTAINER_NAME} -p 8085:8085 ${IMAGE_NAME}:${TAG} &