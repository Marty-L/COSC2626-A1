package com.example.COSC2626_A1.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.COSC2626_A1.model.Song;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class SongRepository {
    final private DynamoDBMapper dynamoDBMapper;

    //Logging adapted from example here: https://www.baeldung.com/slf4j-with-log4j2-logback (viewed 2025-04-16)
    private static final Logger LOGGER = LoggerFactory.getLogger(SongRepository.class);

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

    public Song getSongByTitleArtist(String title, String album){
        return dynamoDBMapper.load(Song.class, title, album);
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

    public List<Song> searchSongs(String title, String artist, String year, String album){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<String> filterExpressions = new ArrayList<>();
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        Map<String, String> expressionAttributeNames = new HashMap<>();

        LOGGER.debug("Title: {}, Artist: {}, Year: {}, Album: {}", title, artist, year, album);

        if (title != null && !title.isEmpty()) {
            filterExpressions.add("contains(title, :title)");
            expressionAttributeValues.put(":title", new AttributeValue().withS(title));
        }

        if (artist != null && !artist.isEmpty()) {
            filterExpressions.add("contains(artist, :artist)");
            expressionAttributeValues.put(":artist", new AttributeValue().withS(artist));
        }

        if (album != null && !album.isEmpty()) {
            filterExpressions.add("contains(album, :album)");
            expressionAttributeValues.put(":album", new AttributeValue().withS(album));
        }

        if (year != null && !year.isEmpty()) {
            filterExpressions.add("#yr = :year");
            expressionAttributeValues.put(":year", new AttributeValue().withS(year));
            expressionAttributeNames.put("#yr", "year");
        }

        if (!filterExpressions.isEmpty()) {
            scanExpression.setFilterExpression(String.join(" and ", filterExpressions));
            scanExpression.setExpressionAttributeValues(expressionAttributeValues);
            if (!expressionAttributeNames.isEmpty()) {
                scanExpression.setExpressionAttributeNames(expressionAttributeNames);
            }
        }

        return dynamoDBMapper.scan(Song.class, scanExpression);
    }

}
