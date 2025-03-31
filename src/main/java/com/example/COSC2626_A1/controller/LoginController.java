package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.User;
import com.example.COSC2626_A1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @Autowired
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String validateLogin(@ModelAttribute User user, Model model) {

        System.out.println("Logging in");
        System.out.println("Email: " + user.getEmail() + "\tPass: " + user.getPassword());
        User validatedUser = userService.validateUser(user.getEmail(), user.getPassword());

        if(validatedUser != null) {
            System.out.println("Logging in:" + user.getEmail());
            //TODO: Redirect to main.html
//            return "redirect:/";
        } else {
            System.out.println("Error logging in: " + user.getUser_name());
            model.addAttribute("errorMessage", "Email or password is invalid");
//            return "login";
        }
        return null;
    }
}