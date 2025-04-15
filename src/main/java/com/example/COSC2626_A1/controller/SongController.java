package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.Song;

import com.example.COSC2626_A1.model.Subscription;
import com.example.COSC2626_A1.service.S3Service;
import com.example.COSC2626_A1.service.SongService;
import com.example.COSC2626_A1.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;

import lombok.AllArgsConstructor;
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

    // TODO: Remove later - Debug purposes
    @GetMapping("/subscribe")
    @ResponseBody
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    // TODO: Remove later - Debug purposes
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
        // Adds returned songs to the model

        //Add the image for each song returned from the search.
        for(Song s : searchedSongs) {
            s.setS3_img_URL(s3Service.getPreSignedImageUrl(s.getS3key()));
        }

        //Add the search results to the session and record a search was made
        session.setAttribute("searchedSongs", searchedSongs);
        session.setAttribute("searchMade", true);

//      TODO: Currently prints the results - Remove later
        System.out.println("Search Results:");
        for (Song s : searchedSongs) {
            System.out.println("Title: " + s.getTitle() +
                    ", Artist: " + s.getArtist() +
                    ", Year: " + s.getYear() +
                    ", Album: " + s.getAlbum());
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
        System.out.println("Email: " + email + " Songs:" + subscription.getSongs().get(0));

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
