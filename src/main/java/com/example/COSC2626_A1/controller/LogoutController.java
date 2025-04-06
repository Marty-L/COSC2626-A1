package com.example.COSC2626_A1.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Invalidate the session
        session.invalidate();

        // Redirect the user to the login page
        //Add a registration success message to the login page and redirect to it.
        redirectAttributes.addFlashAttribute("logoutSuccessMessage",
                "You have been logged out successfully.");
        return "redirect:/login";
    }
}
