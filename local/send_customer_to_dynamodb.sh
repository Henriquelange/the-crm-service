#!/usr/bin/env bash

echo 'Sending DynamoDB Customer document'

awslocal dynamodb put-item --table-name CrmCustomer \
    --item file://$(pwd)/api/src/main/resources/local/customer_dynamodb_document.json
