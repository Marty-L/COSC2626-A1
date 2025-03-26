package com.example.COSC2626_A1.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JsonParserService {

    //Object mapper for reading the JSON tree
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> extractImageURLs(String filePath) throws IllegalArgumentException {
        //Extract the image URLs from the JSON at filePath and return as a List.
        //Will throw IllegalArgumentException if "songs" isn't a JSON array
        //Adapted from example here : https://mkyong.com/java/jackson-how-to-parse-json/#parse-json-array-with-jackson
        //Viewed: 2025-03-26

        List<String> imageURLs = new ArrayList<>();

        try {
            //Get the JSON root and the songs array
            //TODO: Really should be checking root and songsArray for null here...
            JsonNode rootNode = objectMapper.readTree(new File(filePath));
            JsonNode songsArray = rootNode.get("songs");

            //Iterate through the "songs" array and grab the image URLs, adding to the list
            if (songsArray.isArray()) {
                for (JsonNode songNode : songsArray) {
                    String imageURL = songNode.get("img_url").asText();
                    System.out.println("Got URL:" + imageURL); //TODO: Remove this debug print
                    imageURLs.add(imageURL);
                }
            } else throw new IllegalArgumentException("ERROR (parsing JSON): \"songs\" array is not an array");

        } catch (IOException e) {
            //TODO: Probably need better handling for IOException
            e.printStackTrace();
        }

        return imageURLs;
    }
}
