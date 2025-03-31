package com.example.COSC2626_A1.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.COSC2626_A1.model.Song;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SongRepository {
    final private DynamoDBMapper dynamoDBMapper;

    public List<Song> getAllSongs(){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(Song.class, scanExpression);
    }

    public String addSong(Song song){
        dynamoDBMapper.save(song);
        return song.getTitle();
    }

    public Song getSong(String title){
        return dynamoDBMapper.load(Song.class, title);
    }

    public Song updateSong(String title, Song song){
        Song load = dynamoDBMapper.load(Song.class, title);
        load.setArtist(song.getArtist());
        load.setYear(song.getYear());
        load.setImg_url(song.getImg_url());
        load.setAlbum(song.getAlbum());
        dynamoDBMapper.save(load);

        return dynamoDBMapper.load(Song.class, title);
    }

    public void deleteSong(String title){
        Song song = dynamoDBMapper.load(Song.class, title);
        dynamoDBMapper.delete(song);
    }

}
