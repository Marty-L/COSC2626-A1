package com.example.COSC2626_A1.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;


@Service
public class JsonParserService {

    //Object mapper for reading the JSON tree
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LinkedHashSet<String> extractImageURLs(String filePath) throws IllegalArgumentException {
        //Extract the image URLs from the JSON at filePath and return as a List.
        //Will throw IllegalArgumentException if "songs" isn't a JSON array
        //Adapted from example here : https://mkyong.com/java/jackson-how-to-parse-json/#parse-json-array-with-jackson
        //Viewed: 2025-03-26

        //Using LinkedHashSet to prevent duplicates occurring in imageURLs.
        //We only want to download each image from GitHub and upload to S3 once.
        LinkedHashSet<String> imageURLs = new LinkedHashSet<>();

        try {
            //Get the JSON root and the songs array
            JsonNode rootNode = objectMapper.readTree(new File(filePath));
            JsonNode songsArray = rootNode.get("songs");

            //Iterate through the "songs" array and grab the image URLs, adding to the list
            if (songsArray.isArray()) {
                for (JsonNode songNode : songsArray) {
                    String imageURL = songNode.get("img_url").asText();

                    //Debug info: report if we've discarded a duplicate URL.
                    if (imageURLs.add(imageURL)) {
                        System.out.println("Got URL:" + imageURL);
                    } else {
                        System.out.println("Discarded duplicate: " + imageURL);
                    }
                }
            } else throw new IllegalArgumentException("ERROR (parsing JSON): \"songs\" array is not an array");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageURLs;
    }
}
