package com.example.COSC2626_A1.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.COSC2626_A1.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserRepository {
    final private DynamoDBMapper dynamoDBMapper;

    public List<User> getAllUsers() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(User.class, scanExpression);
    }

    public String createUser(User user) {
        dynamoDBMapper.save(user);
        return user.getEmail();
    }

    public User getUser(String email) {
        return dynamoDBMapper.load(User.class, email);
    }

    public User updateUser(String email, User user) {
        User load = dynamoDBMapper.load(User.class, email);
        load.setEmail(user.getEmail());
        load.setUser_name(user.getUser_name());
        load.setPassword(user.getPassword());

        dynamoDBMapper.save(load);

        return dynamoDBMapper.load(User.class, email);
    }

    public void deleteUser(String email) {
        User load = dynamoDBMapper.load(User.class, email);
        dynamoDBMapper.delete(load);
    }


    public User validateUser(String email, String password) {
        //System.out.println("Email: " + email + "\tPass: " + password);
        User user = dynamoDBMapper.load(User.class, email);
        if (user != null && user.getPassword().equals(password)) {
            //System.out.println("Email: " + user.getEmail() + "\tPass: " + user.getPassword());
            return user;
        }
        return null;
    }

    public boolean registerNewUser(User user) {
        //Check if the email already exists.
        //Returns null if no such email.
        User newUser = dynamoDBMapper.load(User.class, user.getEmail());

        if (newUser == null) {
            dynamoDBMapper.save(user);
            System.out.println("Registered new user: " + user.getEmail());
            return true;
        } else {
            return false;
        }
    }
}
