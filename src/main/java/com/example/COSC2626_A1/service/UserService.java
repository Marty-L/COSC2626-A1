package com.example.COSC2626_A1.service;

import com.example.COSC2626_A1.model.User;
import com.example.COSC2626_A1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    final private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public String createCustomer(User user){
        return userRepository.createUser(user);
    }

    public User updateCustomer(String email, User user){
        return userRepository.updateUser(email, user);
    }

    public User getUser(String email){
        return userRepository.getUser(email);
    }

    public void deleteUser(String email){
        userRepository.deleteUser(email);
    }

    public User validateUser(String email, String password){
        return userRepository.validateUser(email, password);
    }

    public boolean registerNewUser(User user){ return userRepository.registerNewUser(user);}
}
