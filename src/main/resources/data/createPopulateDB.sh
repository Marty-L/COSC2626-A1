aws dynamodb create-table \
    --table-name login \
    --attribute-definitions \
        AttributeName=email,AttributeType=S \
    --key-schema \
        AttributeName=email,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --table-class STANDARD

aws dynamodb create-table \
    --table-name music \
    --attribute-definitions \
        AttributeName=title,AttributeType=S \
        AttributeName=album,AttributeType=S \
    --key-schema \
        AttributeName=title,KeyType=HASH \
        AttributeName=album,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --table-class STANDARD

aws dynamodb create-table \
    --table-name subscription \
    --attribute-definitions \
        AttributeName=email,AttributeType=S \
    --key-schema \
        AttributeName=email,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --table-class STANDARD

sleep 10

# Ensure JSON file exists
if [[ ! -f  "login.json" ]]; then
    echo "Error: JSON file login.json not found."
    exit 1
fi


echo "Importing data into DynamoDB table: Login..."

# Read each entry from the JSON file and insert it into DynamoDB
jq -c '.login[]' "login.json" | while IFS= read -r item; do
    email=$(echo "$item" | jq -r '.email')
    user_name=$(echo "$item" | jq -r '.user_name')
    password=$(echo "$item" | jq -r '.password')

    aws dynamodb put-item --table-name "login" --item "{
        \"email\": {\"S\": \"$email\"},
        \"user_name\": {\"S\": \"$user_name\"},
        \"password\": {\"S\": \"$password\"}
    }"
done

echo "User data import complete."

#Error checking
if [[ ! -f "2025a1.json" ]]; then
    echo "Error: JSON file 2025a1.json not found."
    exit 1
fi

echo "Importing data into DynamoDB table: Music..."


jq -c '.songs[]' "2025a1.json" | while IFS= read -r item; do
    echo "entered"
    echo "$item"
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

echo "Music data import complete"
