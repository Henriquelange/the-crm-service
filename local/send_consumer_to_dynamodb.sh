#!/usr/bin/env bash

echo 'Sending DynamoDB Consumer document'

awslocal dynamodb put-item --table-name CrmConsumer \
    --item file://$(pwd)/api/src/main/resources/local/consumer_dynamodb_document.json
