package com.example.COSC2626_A1.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.COSC2626_A1.model.Subscription;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SubscriptionRepository {
    final private DynamoDBMapper dynamoDBMapper;

    public List<Subscription> getAllSubscriptions(){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(Subscription.class, scanExpression);
    }

    public String addSubscription(Subscription subscription){
        dynamoDBMapper.save(subscription);
        return subscription.getEmail();
    }

    public Subscription getSubscription(String email){return dynamoDBMapper.load(Subscription.class, email);}

    public Subscription updateSubscription(String email , Subscription subscription){
        Subscription load = dynamoDBMapper.load(Subscription.class, email);
        load.setEmail(subscription.getEmail());
        load.setSongs(subscription.getSongs());
        dynamoDBMapper.save(load);

        return dynamoDBMapper.load(Subscription.class, email);
    }

}
