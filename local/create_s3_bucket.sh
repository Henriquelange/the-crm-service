#!/usr/bin/env bash

awslocal s3api create-bucket \
  --bucket agile-monkeys-crm-customer \
  --region us-east-1

wait
echo "S3 bucket created."
