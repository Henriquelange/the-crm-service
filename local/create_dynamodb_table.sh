#!/usr/bin/env bash

create_db_table() {
	while read t; do
	    read attributes;
	    read key_schema;
	    read throughput;
	    echo "Creating $t"
		  awslocal dynamodb create-table \
          --table-name $t \
          --attribute-definitions $attributes \
          --key-schema $key_schema \
          --provisioned-throughput $throughput
	done < $1
}

for file in local/dynamodb_table_definitions.txt; do
  if [ -f $file ]; then
    create_db_table $file
  fi
done


wait
echo "DynamoDB tables created."
