#!/bin/bash

export CI_COMMIT_REF_NAME=MF-443

# deploy shared services
docker stack deploy -c infra.yaml infra --detach

# build the application container image
docker build \
  --build-arg CI_COMMIT_REF_NAME=${CI_COMMIT_REF_NAME} \
  -t my-app:$CI_COMMIT_REF_NAME .


# Create application database for the feature branch deployment

export SERVICE_ID=$(docker service ps infra_pgsql -q)
export CONTAINER_ID=$(docker inspect ${SERVICE_ID} --format '{{.Status.ContainerStatus.ContainerID}}')

docker exec -it ${CONTAINER_ID} createdb -U postgres app_${CI_COMMIT_REF_NAME} || true

# Create rabbitmq resources for the feature branch deployment

CI_COMMIT_REF_NAME=MF-443 && \
export SERVICE_ID=$(docker service ps infra_rabbitmq -q)
export CONTAINER_ID=$(docker inspect ${SERVICE_ID} --format '{{.Status.ContainerStatus.ContainerID}}')
docker exec -it ${CONTAINER_ID} rabbitmqctl add_vhost ${CI_COMMIT_REF_NAME}
docker exec -it ${CONTAINER_ID} rabbitmqctl add_user ${CI_COMMIT_REF_NAME}_user YourStrongPassword
docker exec -it ${CONTAINER_ID} rabbitmqctl set_user_tags ${CI_COMMIT_REF_NAME}_user
docker exec -it ${CONTAINER_ID} rabbitmqctl set_permissions -p ${CI_COMMIT_REF_NAME} \
  ${CI_COMMIT_REF_NAME}_user \
  ".*" ".*" ".*"

# deploy the application
docker stack deploy -c app.yaml app-${CI_COMMIT_REF_NAME} --detach