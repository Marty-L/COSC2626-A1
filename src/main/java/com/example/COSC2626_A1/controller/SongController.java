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

    @GetMapping("/songs")
    @ResponseBody
    public List<Song> getAllSongs(){
        return songService.getAllSongs();
    }

    @PostMapping("/search")
    public String search(
            @ModelAttribute Song song,
            Model model,
            HttpSession session) {
// TODO: not added session
        List<Song> songs = songService.searchSongs(
                song.getTitle(),
                song.getArtist(),
                song.getYear(),
                song.getAlbum());

        model.addAttribute("songs", songs);
        model.addAttribute("searchMade", true);
        System.out.println(songs);
        return "main"; // Renders templates/main.html with results
    }
//    @PostMapping("/search")
//    public String search(Model model, HttpSession session, @ModelAttribute Song song) {
//        //TODO: Add functionality to search for a song
//        System.out.println(song);
//
//        List<Song> songs = songService.searchSongs(song.getTitle(), song.getArtist(), song.getYear(), song.getAlbum());
//
//        model.addAttribute("songs", songs);
//
//        //Set a session attribute to indicate a search has been made
//        session.setAttribute("searchMade", true);
//        return "redirect:/main";
//    }
}
