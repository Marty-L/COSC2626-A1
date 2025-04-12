package com.example.COSC2626_A1.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@DynamoDBTable(tableName = "subscription")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    @DynamoDBHashKey(attributeName = "email")
    private String email;

    @DynamoDBAttribute(attributeName = "songs")
    private List<SubSong> songs;

    @DynamoDBDocument
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SubSong {
        @DynamoDBAttribute
        private String title;

        @DynamoDBAttribute
        private String album;
    }

}
