package com.example.COSC2626_A1.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Service
public class ImageDownloaderService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Path> downloadImages(LinkedHashSet<String> imageURLs) {
        //Download the images from GitHub links in the JSON and return list of fileNames.
        //Adapted from examples:
        // * https://scrapingant.com/blog/download-image-java (Viewed: 2025-03-30)
        // * https://howtodoinjava.com/java/io/create-a-temporary-file-in-java/ (Viewed: 2025-03-30)

        //Keep a record of where the temporary images are stored, using imageURLs as keys.
        Map<String, Path> imageFiles = new HashMap<>();

        try {
            //Temporary storage - delegate the job of figuring out the location to the OS.
            //Give the directory a nice prefix so we can find them easily
            Path tempDir = Files.createTempDirectory("COSC26262-A1-tmp-img-");

            for (String imageURL : imageURLs) {
                //Extract just the filename from the URL
                String fileName = imageURL.substring(imageURL.lastIndexOf("/") + 1);

                try {
                    //Generate an HTTP request for the image URL (returns response body as byte array)
                    ResponseEntity<byte[]> response = restTemplate.getForEntity(imageURL, byte[].class);

                    //Check the request was successful - i.e.: HTTP Status = 2xx and response body is not null.
                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

                        //Write the image content from the HTTP response body to temp file.
                        //Provide null for file name prefix - allow the OS pick a temp name for us.
                        Path tempFile = Files.createTempFile(tempDir, null,".jpg");
                        Files.write(tempFile, response.getBody());

                        //Update the record of where the temp file is stored
                        imageFiles.put(fileName, tempFile);
                        System.out.println("Image downloaded: " + imageURL + " -> " + tempFile);

                    } else {
                        System.err.println("Failed to download " + imageURL);
                    }
                } catch (RestClientException e) {
                    System.err.println("REST exception whilst downloading " + imageURL);
                } catch (IllegalArgumentException e) {
                    System.err.println("Unable to add " + imageURL + " to list (illegal argument)");
                } catch (IOException e) {
                    System.err.println("IO error whilst writing to file " + fileName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating the temporary local storage directory");
        }

        return imageFiles;
    }
}
