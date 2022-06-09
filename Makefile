SHELL = /bin/bash

.DEFAULT_GOAL := help

help:
	echo "This is the makefile. Run docker-start to get the local infrastructure up and running."

# Create and start Docker local infrastructure
docker-start:
	docker-compose -f local/docker-compose.yaml up -d --remove-orphans
	@sleep 2
	sh local/create_dynamodb_table.sh
	@sleep 1
	sh local/send_customer_to_dynamodb.sh

# Start Docker local infrastructure
docker-start-only:
	docker-compose -f local/docker-compose.yaml up -d --remove-orphans

# Stop Docker
docker-stop:
	docker-compose -f local/docker-compose.yaml stop -t 0

# Stop and delete Docker local infrastructure
docker-rm: docker-stop
	docker-compose -f local/docker-compose.yaml rm -fv
	docker network prune
