#!/bin/bash
export CI_COMMIT_REF_NAME=MF-443

# stop / remove the application service
docker stack rm app-${CI_COMMIT_REF_NAME} --detach

# Drop the application database
export DB_SERVICE_ID=$(docker service ps infra_pgsql -q)
export DB_CONTAINER_ID=$(docker inspect ${DB_SERVICE_ID} --format '{{.Status.ContainerStatus.ContainerID}}')

docker exec -it ${DB_CONTAINER_ID} dropdb -U postgres app_${CI_COMMIT_REF_NAME} || true

# Remove rabbitmq resources
export RABBITMQ_SERVICE_ID=$(docker service ps infra_rabbitmq -q)
export RABBITMQ_CONTAINER_ID=$(docker inspect ${RABBITMQ_SERVICE_ID} --format '{{.Status.ContainerStatus.ContainerID}}')

docker exec -it ${RABBITMQ_CONTAINER_ID} rabbitmqctl delete_vhost ${CI_COMMIT_REF_NAME} || true
docker exec -it ${RABBITMQ_CONTAINER_ID} rabbitmqctl delete_user ${CI_COMMIT_REF_NAME}_user || true