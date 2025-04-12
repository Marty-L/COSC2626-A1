package com.example.COSC2626_A1.service;

import com.example.COSC2626_A1.model.Song;
import com.example.COSC2626_A1.repository.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SongService {
    final private SongRepository songRepository;

    public List<Song> getAllSongs(){
        return songRepository.getAllSongs();
    }

    public String addSong(Song song){
        return songRepository.addSong(song);
    }

    public Song updateSong(String title, Song song){
        return songRepository.updateSong(title, song);
    }

    public Song getSong(String title){
        return songRepository.getSong(title);
    }

    public Song getSongByTitleArtist(String title, String album){
        return songRepository.getSongByTitleArtist(title, album);
    }

    public void deleteSong(String title){
        songRepository.deleteSong(title);
    }

    public List<Song> searchSongs(String title, String artist, String year, String album){ return songRepository.searchSongs(title, artist, year, album); }
}
