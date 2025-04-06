package com.example.COSC2626_A1.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.COSC2626_A1.model.Song;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    public List<Song> searchSongs(String title, String artist, String year, String album){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        List<String> filterExpressions = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            filterExpressions.add("contains(title, :title)");
        }
        if (artist != null && !artist.isEmpty()) {
            filterExpressions.add("contains(artist, :artist)");
        }
        if (album != null && !album.isEmpty()) {
            filterExpressions.add("contains(album, :album)");
        }
        if (year != null) {
            filterExpressions.add("year = :year");
        }

        if (!filterExpressions.isEmpty()) {
            scanExpression.setFilterExpression(String.join(" and ", filterExpressions));
        }

        // Add values to the filter expression (e.g., for :title, :artist, etc.)
        scanExpression.addExpressionAttributeValuesEntry(":title", new AttributeValue().withS(title));
        scanExpression.addExpressionAttributeValuesEntry(":artist", new AttributeValue().withS(artist));
        scanExpression.addExpressionAttributeValuesEntry(":album", new AttributeValue().withS(album));
        scanExpression.addExpressionAttributeValuesEntry(":year", new AttributeValue().withS(year));

        // Perform the scan and return the results
        return dynamoDBMapper.scan(Song.class, scanExpression);
    }

}
