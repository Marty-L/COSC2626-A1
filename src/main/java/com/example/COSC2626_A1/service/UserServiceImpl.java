package com.example.COSC2626_A1.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.COSC2626_A1.model.User;
import com.example.COSC2626_A1.model.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServiceBL{

    private final DynamoDBMapper dynamoDBMapper;

    public UserServiceImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }
    @Override
    public boolean validateLogin(UserDTO userDTO) {
        //Attempt to pull user from the DB using email.
        //Will return null if no such email.
        User user = dynamoDBMapper.load(User.class, userDTO.getEmail());

        //Check password for the retrieved user.
        return user != null && user.getPassword().equals(userDTO.getPassword());
    }

    @Override
    public boolean registerNewUser(UserDTO newUserDTO) {
        //Check if the email already exists.
        //Returns null if no such email.
        User newUser = dynamoDBMapper.load(User.class, newUserDTO.getEmail());

        if (newUser == null) {
            User user = new User();
            BeanUtils.copyProperties(newUserDTO, user);
            dynamoDBMapper.save(user);
            System.out.println("Registered new user: " + newUserDTO.getEmail());
            return true;
        } else {
            return false;
        }
    }

//    @Override
//    public List<UserDTO> getAllUsers() {
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//        List<User> users = dynamoDBMapper.scan(User.class, scanExpression);
//        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }

//    @Override
//    public UserDTO getUserByEmail(String email) {
//        User user = dynamoDBMapper.load(User.class, email);
//        return convertToDTO(user);
//    }

//    @Override
//    public UserDTO getUserByUsername(String user_name) {
//        User user = dynamoDBMapper.load(User.class, user_name);
//        return convertToDTO(user);
//    }

//    @Override
//    public UserDTO createNewUser(UserDTO userDTO) {
//        User user = new User();
//        BeanUtils.copyProperties(userDTO, user);
//
//        dynamoDBMapper.save(user);
//
//        return convertToDTO(user);
//    }



//    @Override
//    public UserDTO updateUser(String email, UserDTO userDTO) {
//        User user = dynamoDBMapper.load(User.class, email);
//        BeanUtils.copyProperties(userDTO, user);
//
//        dynamoDBMapper.save(user);
//
//        return convertToDTO(user);
//    }

//    @Override
//    public void deleteUser(String email) {
//        User user = dynamoDBMapper.load(User.class, email);
//        if (user != null) {
//            dynamoDBMapper.delete(user);
//        }
//    }

//    private UserDTO convertToDTO(User user) {
//        UserDTO userDTO = new UserDTO();
//        BeanUtils.copyProperties(user, userDTO);
//        return userDTO;
//    }

}
