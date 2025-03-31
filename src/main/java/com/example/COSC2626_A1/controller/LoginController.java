package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.UserDTO;
import com.example.COSC2626_A1.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showLoginForm(@ModelAttribute("userDTO") UserDTO userDTO) {
        return "/login";
    }

    @PostMapping("/login")
    public String processLogin(Model model,
                               @ModelAttribute("userDTO") UserDTO userDTO) {
        //Check user email and password match a valid user.
        boolean loginSuccess = userService.validateLogin(userDTO);

        if(loginSuccess) {
            System.out.println("Logging in:" + userDTO.getEmail());
            //TODO: Re-direct to main.mtl upon success
            return "redirect:/home.html";
        } else {
            System.out.println("Error logging in: " + userDTO.getUser_name());
            model.addAttribute("errorMessage", "Email or password is invalid");
            return "redirect:/login.html";
        }
    }
}
