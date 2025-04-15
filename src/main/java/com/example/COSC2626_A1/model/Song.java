package com.example.COSC2626_A1.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
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
    @DynamoDBRangeKey(attributeName = "album")
    private String album;
    @DynamoDBAttribute
    private String year;
    @DynamoDBAttribute
    private String artist;
    @DynamoDBAttribute
    private String img_url;
    @DynamoDBIgnore
    private String s3_img_URL;

    public String getS3key() {
        //Return the 'filename.jpg' portion of the raw GitHub link...
        //...this will be the key used to store this song's image on s3.
        return img_url.substring(img_url.lastIndexOf("/") + 1);
    }
}
