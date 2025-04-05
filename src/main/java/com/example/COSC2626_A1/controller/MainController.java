package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    //TODO: Add main.html

    //TODO: Add main GET handler
    @GetMapping("/main")
    public String login(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
            return "main";
        } else {
            System.out.println("Error: user not present in session data.");
            return "redirect:/login";
        }
    }
}

