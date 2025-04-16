package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.User;
import com.example.COSC2626_A1.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class LoginController {
    private final UserService userService;

    //Logging adapted from example here: https://www.baeldung.com/slf4j-with-log4j2-logback (viewed 2025-04-16)
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String validateLogin(Model model, HttpSession session, @ModelAttribute User user) {

        LOGGER.debug("Email: {}\tPass: {}", user.getEmail(), user.getPassword());
        User validatedUser = userService.validateUser(user.getEmail(), user.getPassword());

        if(validatedUser != null) {
            LOGGER.info("Logging in:{}\t with password: {}", user.getEmail(), user.getPassword());
            session.setAttribute("user", validatedUser); //Add the validated user to the session
            return "redirect:/main";
        } else {
            LOGGER.error("Error logging in: {}", user.getUser_name());
            model.addAttribute("loginErrorMessage", "Email or password is invalid");
            return "login";
        }
    }
}