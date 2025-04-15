package com.example.COSC2626_A1.config;


import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {
    // Initialising AWS variables from application.properties
    @Value("${aws.dynamodb.endpoint}")
    private String awsDynamoDBEndPoint;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.access.secret-key}")
    private String awsSecretKey;

    @Value("${aws.sessionToken}")
    private String dynamodbSessionToken;

    public AWSCredentialsProvider amazonAWSCredentialsProvider(){
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean
    public AWSCredentials amazonAWSCredentials(){
        return new BasicSessionCredentials(awsAccessKey, awsSecretKey, dynamodbSessionToken);
    }


//      Initialising the connection from Springboot to the DynamoDB server.
    @Bean
    public AmazonDynamoDB amazonDynamoDB(){
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(amazonAWSCredentialsProvider())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint, awsRegion))
                .build();
    }

    @Bean
    public DynamoDBMapper mapper(){
        return new DynamoDBMapper(amazonDynamoDB());
    }
}
