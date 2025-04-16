package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.User;
import com.example.COSC2626_A1.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    @Autowired
    private final UserService userService;

    //Logging adapted from example here: https://www.baeldung.com/slf4j-with-log4j2-logback (viewed 2025-04-16)
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "/register";
    }

    @PostMapping("/register")
    public String processRegistration( Model model,
                                       @ModelAttribute("user") User user,
                                       RedirectAttributes redirectAttributes,
                                       @RequestParam("confirm-password") String confirmPassword) {
        //Check password and confirmPassword match.
        if (!user.getPassword().equals(confirmPassword)) {
            //Add an error message and stay on register page.
            model.addAttribute("passwordErrorMessage", "Passwords do not match!");
            model.addAttribute("user", user);
            return "redirect:/register"; // Reload the form with an error message
        }

        //Check if we can register this new user.
        boolean registrationSuccess = userService.registerNewUser(user);

        if(registrationSuccess) {
            LOGGER.info("Registered: {} with email: {} and password: {}",
                    user.getUser_name(), user.getUser_name(), user.getPassword());

            //Add a registration success message to the login page and redirect to it.
            redirectAttributes.addFlashAttribute("registrationSuccessMessage",
                    "Registration successful! Please log in.");
            return "redirect:/login";
        } else {
            //Add an error message and stay on register page.
            model.addAttribute("emailErrorMessage", "User email already exists.");
            model.addAttribute("user", user);
            return "register";
        }
    }

}
