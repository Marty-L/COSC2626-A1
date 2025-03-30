package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.UserDTO;
import com.example.COSC2626_A1.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @Autowired
    private final UserServiceImpl userService;

    public LoginController(UserServiceImpl userServiceImpl) {
        this.userService = userServiceImpl;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
        boolean loginSuccess = userService.validateLogin(userDTO.getEmail(), userDTO.getPassword());

        if(loginSuccess) {
            System.out.println("Logging in:" + userDTO.getEmail());
            return "redirect:/";
        } else {
            System.out.println("Error logging in: " + userDTO.getUser_name());
            model.addAttribute("errorMessage", "Email or password is invalid");
            return "login";
        }
    }
}
