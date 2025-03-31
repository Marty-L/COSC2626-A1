package com.example.COSC2626_A1.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Define the table and schema for the music table.
@DynamoDBTable(tableName = "music")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @DynamoDBHashKey(attributeName = "title")
    private String title;
    @DynamoDBRangeKey(attributeName = "artist")
    private String artist;
    @DynamoDBAttribute
    private String year;
    @DynamoDBAttribute
    private String album;
    @DynamoDBAttribute
    private String img_url;

}
