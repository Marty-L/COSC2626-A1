package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.UserDTO;
import com.example.COSC2626_A1.service.UserServiceBL;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

// Rest controller for calling CRUD functions DynamoDB
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceBL userServiceBL;
    private final SpringTemplateEngine springTemplateEngine;

    public UserController(UserServiceBL userServiceBL, SpringTemplateEngine springTemplateEngine) {
        this.userServiceBL = userServiceBL;
        this.springTemplateEngine = springTemplateEngine;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userServiceBL.getAllUsers();
    }

    @GetMapping("/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        return userServiceBL.getUserByEmail(email);
    }

// TODO: Decide whether username will be a parameter for @GetMapping

//    @GetMapping("/{username}")
//    public UserDTO getUserByUsername(@PathVariable String username) {
//        return userServiceBL.getUserByUsername(username);
//    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userServiceBL.createNewUser(userDTO);
    }

    @PutMapping("/{email}")
    public UserDTO updateUser(@PathVariable String email, @RequestBody UserDTO userDTO) {
        return userServiceBL.updateUser(email, userDTO);
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        userServiceBL.deleteUser(email);
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, Model model) {
        boolean isValid = userServiceBL.validateLogin(userDTO.getUser_name(), userDTO.getPassword());

        if (isValid) {
            return "redirect:/home"; // Redirect to the  on success
        } else {
            model.addAttribute("error", "Invalid email or password");
            return null; // Return to login page with an error message
        }
    }

}
