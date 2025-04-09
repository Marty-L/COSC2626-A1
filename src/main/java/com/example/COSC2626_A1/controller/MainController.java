package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.Song;
import com.example.COSC2626_A1.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/main")
    public String login(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Boolean searchMade = (Boolean) session.getAttribute("searchMade");

        if (user != null) {
            model.addAttribute("user", user);

            //If searchMade wasn't already in the session attributes, a search hasn't been made yet.
            model.addAttribute("searchMade", searchMade != null && searchMade);

            // Add a Song to the model - needed for searching
            model.addAttribute("song", new Song());
            return "main";
        } else {
            System.out.println("Error: user not present in session data.");
            return "redirect:/login";
        }
    }
}

