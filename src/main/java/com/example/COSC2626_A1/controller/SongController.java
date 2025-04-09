package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.Song;

import com.example.COSC2626_A1.service.SongService;
import jakarta.servlet.http.HttpSession;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
public class SongController {

    private final SongService songService;

    // TODO: Remove Later
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
        model.addAttribute("searchedSongs", searchedSongs);

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
        model.addAttribute("searchMade", true);
        return "main"; // Renders templates/main.html with results
    }
}
