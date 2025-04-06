# execte.ps1
$jsonFile = "login.json"
$tableName = "login"

#aws dynamodb create-table --table-name login --attribute-definitions AttributeName=email,AttributeType=S --key-schema AttributeName=email,KeyType=HASH --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 --table-class STANDARD --endpoint-url http://localhost:8000

# Read JSON file
$jsonContent = Get-Content $jsonFile | ConvertFrom-Json

# Loop through each login entry
foreach ($item in $jsonContent.login) {
    $email = $item.email
    $userName = $item.user_name
    $password = $item.password

    # Construct the item JSON with proper formatting
    $itemJson = (@{
        email      = @{ S = $email }
        user_name  = @{ S = $userName }
        password   = @{ S = $password }
    } | ConvertTo-Json -Compress)
#   } | ConvertTo-Json -Compress) -replace '"', '\"'

    # AWS CLI command for local DynamoDB
#     aws --profile=dynamodb-localdev dynamodb put-item --endpoint-url http://localhost:8000 --table-name $tableName --item "$itemJson"
    aws dynamodb put-item --table-name $tableName --item "$itemJson" --endpoint-url http://localhost:8000

}
 