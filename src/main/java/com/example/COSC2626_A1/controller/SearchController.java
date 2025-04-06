package com.example.COSC2626_A1.controller;

import com.example.COSC2626_A1.model.Song;

import com.example.COSC2626_A1.repository.SongRepository;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SearchController {

    private final SongRepository songRepository;

    public SearchController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }


    @PostMapping("/search")
    public String search(Model model, HttpSession session, @ModelAttribute Song song) {
        //TODO: Add functionality to search for a song
        System.out.println(song);

        List<Song> songs = songRepository.searchSongs(song.getTitle(), song.getArtist(), song.getYear(), song.getAlbum());

        model.addAttribute("songs", songs);

        //Set a session attribute to indicate a search has been made
        session.setAttribute("searchMade", true);
        return "redirect:/main";
    }
}
