package com.example.COSC2626_A1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    //TODO: Add validation for createUser()
    //TODO: Error: "User already exists" (based off EMAIL)

}
