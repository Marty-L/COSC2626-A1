#!/bin/bash

aws dynamodb create-table \
    --table-name music \
    --attribute-definitions \
        AttributeName=title,AttributeType=S \
        AttributeName=artist,AttributeType=S \
    --key-schema \
        AttributeName=artist,KeyType=HASH \
        AttributeName=title,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --table-class STANDARD

#Error checking
if [[ ! -f "2025a1.json" ]]; then
    echo "Error: JSON file 2025a1.json not found."
    exit 1
fi
jq -c '.songs[]' "2025a1.json" | while IFS= read -r item; do

    title=$(echo "$item" | jq -r '.title')
    artist=$(echo "$item" | jq -r '.artist')
    year=$(echo "$item" | jq -r '.year')
    album=$(echo "$item" | jq -r '.album')
    img_url=$(echo "$item" | jq -r '.img_url')

    aws dynamodb put-item --table-name "music" --item "{
        \"title\": {\"S\": \"$title\"},
        \"artist\": {\"S\": \"$artist\"},
        \"year\": {\"S\": \"$year\"},
        \"album\": {\"S\": \"$album\"},
        \"img_url\": {\"S\": \"$img_url\"}
    }"
done