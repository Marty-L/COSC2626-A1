package com.example.COSC2626_A1.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import com.example.COSC2626_A1.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserRepository {
    final private DynamoDBMapper dynamoDBMapper;

    //Logging adapted from example here: https://www.baeldung.com/slf4j-with-log4j2-logback (viewed 2025-04-16)
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

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
        LOGGER.debug("Email: {}\tPass: {}", email, password);
        User user = dynamoDBMapper.load(User.class, email);
        if (user != null && user.getPassword().equals(password)) {
            LOGGER.debug("Email: {}\tPass: {}", user.getEmail(), user.getPassword());
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
            LOGGER.debug("Registered new user: {}", user.getEmail());
            return true;
        } else {
            return false;
        }
    }
}
