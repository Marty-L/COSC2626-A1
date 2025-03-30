package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "home";
    }


    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}