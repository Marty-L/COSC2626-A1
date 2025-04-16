package com.example.COSC2626_A1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;


@Service
public class JsonParserService {

    //Object mapper for reading the JSON tree
    private final ObjectMapper objectMapper = new ObjectMapper();

    //Logging adapted from example here: https://www.baeldung.com/slf4j-with-log4j2-logback (viewed 2025-04-16)
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParserService.class);

    public LinkedHashSet<String> extractImageURLs(InputStream inputStream) throws IllegalArgumentException {
        //Extract the image URLs from the JSON at filePath and return as a List.
        //Will throw IllegalArgumentException if "songs" isn't a JSON array
        //Adapted from example here : https://mkyong.com/java/jackson-how-to-parse-json/#parse-json-array-with-jackson
        //Viewed: 2025-03-26

        //Using LinkedHashSet to prevent duplicates occurring in imageURLs.
        //We only want to download each image from GitHub and upload to S3 once.
        LinkedHashSet<String> imageURLs = new LinkedHashSet<>();

        try {
            //Get the JSON root and the songs array
            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode songsArray = rootNode.get("songs");

            //Iterate through the "songs" array and grab the image URLs, adding to the list
            if (songsArray.isArray()) {
                for (JsonNode songNode : songsArray) {
                    String imageURL = songNode.get("img_url").asText();

                    //Debug info: report if we've discarded a duplicate URL.
                    if (imageURLs.add(imageURL)) {
                        LOGGER.debug("Got URL:{}", imageURL);
                    } else {
                        LOGGER.debug("Discarded duplicate: {}", imageURL);
                    }
                }
            } else throw new IllegalArgumentException("ERROR (parsing JSON): \"songs\" array is not an array");

        } catch (IOException e) {
            LOGGER.error("ERROR [{}]:", this.getClass().getName(), e);
        }

        return imageURLs;
    }
}
