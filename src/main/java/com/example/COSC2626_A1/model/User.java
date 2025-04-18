package com.example.COSC2626_A1.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Define the table and schema for the login table.
@Data //Provides getter and setter for all fields (Lombok)
@DynamoDBTable(tableName = "login")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @DynamoDBHashKey(attributeName = "email")
    private String email;

    @DynamoDBAttribute(attributeName = "user_name")
    private String user_name;

    @DynamoDBAttribute(attributeName = "password")
    private String password;

}
