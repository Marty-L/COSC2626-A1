#!/bin/bash

JSON_FILE="login.json"

# Ensure JSON file exists
if [[ ! -f "$JSON_FILE" ]]; then
    echo "Error: JSON file $JSON_FILE not found."
    exit 1
fi

aws dynamodb create-table \
    --table-name login \
    --attribute-definitions \
        AttributeName=email,AttributeType=S \
    --key-schema \
        AttributeName=email,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --table-class STANDARD


# Read each entry from the JSON file and insert it into DynamoDB
jq -c '.login[]' "$JSON_FILE" | while IFS= read -r item; do
    email=$(echo "$item" | jq -r '.email')
    user_name=$(echo "$item" | jq -r '.user_name')
    password=$(echo "$item" | jq -r '.password')

    aws dynamodb put-item --table-name "login" --item "{
        \"email\": {\"S\": \"$email\"},
        \"user_name\": {\"S\": \"$user_name\"},
        \"password\": {\"S\": \"$password\"}
    }"
done

echo "Data import complete."
