package com.example.COSC2626_A1.service;

import com.example.COSC2626_A1.model.UserDTO;

import java.util.List;

public interface UserServiceBL {
    List<UserDTO> getAllUsers();

    UserDTO getUserByEmail(String email);

//    UserDTO getUserByUsername(String username);

    UserDTO createNewUser(UserDTO user);

    UserDTO updateUser(String email, UserDTO user);

    void deleteUser(String email);

    boolean validateLogin(String username, String password);

}
