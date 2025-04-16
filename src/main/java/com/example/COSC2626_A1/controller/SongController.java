package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.Song;

import com.example.COSC2626_A1.model.Subscription;
import com.example.COSC2626_A1.service.S3Service;
import com.example.COSC2626_A1.service.SongService;
import com.example.COSC2626_A1.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Controller
public class SongController {

    private final SongService songService;
    private final SubscriptionService subscriptionService;
    private final S3Service s3Service;

    //Logging adapted from example here: https://www.baeldung.com/slf4j-with-log4j2-logback (viewed 2025-04-16)
    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);

    @GetMapping("/subscribe")
    @ResponseBody
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @GetMapping("/songs")
    @ResponseBody
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }

    @PostMapping("/search")
    public String search(@ModelAttribute Song song, Model model, HttpSession session) {
        List<Song> searchedSongs = songService.searchSongs(
                song.getTitle(),
                song.getArtist(),
                song.getYear(),
                song.getAlbum());

        //Add the search results to the session and record a search was made
        session.setAttribute("searchedSongs", searchedSongs);
        session.setAttribute("searchMade", true);

        //Add the image for each song returned from the search.
        for(Song s : searchedSongs) {
            s.setS3_img_URL(s3Service.getPreSignedImageUrl(s.getS3key()));
        }

        LOGGER.debug("Search Results:");
        for (Song s : searchedSongs) {
            LOGGER.debug("Title: {}, Artist: {}, Year: {}, Album: {}",
                    s.getTitle(), s.getArtist(), s.getYear(), s.getAlbum());
        }

        // Retaining user in the model after a search
        Object user = session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        return "redirect:/main"; // Re-direct to main with results set in session
    }

    @PostMapping("/subscription/add")
    public String addSubscription(@ModelAttribute Subscription subscription, HttpSession session) {
        String email = subscription.getEmail();
        Subscription.SubSong newSong = subscription.getSongs().get(0);
        LOGGER.debug("Email: {} Songs: {}", email, subscription.getSongs().get(0));

        Subscription existing = subscriptionService.getSubscription(email);
        if (existing == null) {
            // If no subscription exists, create one with an empty songs list
            existing = new Subscription();
            existing.setEmail(email);
            existing.setSongs(new ArrayList<>());
            subscriptionService.addSubscription(existing);
        } else if (existing.getSongs() == null) {
            // Ensure songs list isn't null
            existing.setSongs(new ArrayList<>());
        }

        List<Subscription.SubSong> songs = existing.getSongs();
        // Checks to see if this song is in the subscription already
        if (songs != null){
            songs.add(newSong);
            subscriptionService.updateSubscription(email, existing);
        }
        // Save updated subscription into session only
        session.setAttribute("subscription", existing);
        return "redirect:/main";

    }

    @PostMapping("/subscription/remove")
    public String removeSubscribedSong(@ModelAttribute Subscription subscription, HttpSession session) {
        Subscription.SubSong songToRemove = subscription.getSongs().get(0);

        Subscription existing = subscriptionService.getSubscription(subscription.getEmail());
        if (existing != null && existing.getSongs() != null) {
            existing.getSongs().removeIf(
                    s -> s.getTitle().equals(songToRemove.getTitle()) && s.getAlbum().equals(songToRemove.getAlbum()));
            subscriptionService.updateSubscription(existing.getEmail(), existing);
        }
        // Save subs to session
        session.setAttribute("subscription", existing);
        return "redirect:/main";
    }

 }
