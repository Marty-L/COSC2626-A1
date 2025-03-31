package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.User;
import com.example.COSC2626_A1.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Rest controller for calling CRUD functions DynamoDB
@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/user")
    public String createUser(@RequestBody User user){
        return userService.createCustomer(user);
    }


    @GetMapping("/user/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userService.getUser(email);
    }

    @PutMapping("/user/{email}")
    public User updateUser(@PathVariable String email, @RequestBody User user){
        return userService.updateCustomer(email, user);
    }

    @DeleteMapping("/user/{email}")
    public void deleteUser(@PathVariable String email){
        userService.deleteUser(email);
    }

}
