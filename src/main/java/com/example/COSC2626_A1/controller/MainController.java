package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.Song;
import com.example.COSC2626_A1.model.Subscription;
import com.example.COSC2626_A1.model.User;
import com.example.COSC2626_A1.service.S3Service;
import com.example.COSC2626_A1.service.SongService;
import com.example.COSC2626_A1.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    private final SubscriptionService subscriptionService;
    private final SongService songService;
    private final S3Service s3Service;

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

            // Retrieve email to get subscription details
            Subscription subscription = subscriptionService.getSubscription(user.getEmail());
            List<Song> subscribedSongs = new ArrayList<>();
            if (subscription != null && subscription.getSongs() != null) {
                for (Subscription.SubSong sub : subscription.getSongs()) {
                    Song fullSong = songService.getSongByTitleArtist(sub.getTitle(), sub.getAlbum());
                    if (fullSong != null) {
                        fullSong.setS3_img_URL(s3Service.getPreSignedImageUrl(fullSong.getS3key()));
                        subscribedSongs.add(fullSong);
                    }
                }
            }
            model.addAttribute("subscribedSongs", subscribedSongs);


            return "main";
        } else {
            System.out.println("Error: user not present in session data.");
            return "redirect:/login";
        }
    }
}

