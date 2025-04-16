package com.example.COSC2626_A1;

import com.example.COSC2626_A1.service.ImageDownloaderService;
import com.example.COSC2626_A1.service.JsonParserService;

import com.example.COSC2626_A1.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private final JsonParserService jsonParserService;

	@Autowired
	private final S3Service s3Service;

	@Autowired
	private final ImageDownloaderService imageDownloaderService;

	//Logging adapted from example here: https://www.baeldung.com/slf4j-with-log4j2-logback (viewed 2025-04-16)
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public Application(JsonParserService jsonParserService, S3Service s3Service,
					   ImageDownloaderService imageDownloaderService) {
		this.jsonParserService = jsonParserService;
		this.s3Service = s3Service;
		this.imageDownloaderService = imageDownloaderService;
	}

	//RUN THE MAIN SERVER APPLICATION
	public static void main(String[] args) {SpringApplication.run(Application.class, args);}

	@Override
	public void run(String... args) {
		try {
			// Use ClassPathResource to load the JSON file from the classpath
			Resource resource = new ClassPathResource("data/2025a1.json");
			try (InputStream inputStream = resource.getInputStream()) {
				// Parse the JSON file and grab the image URLs for downloading from GitHub
				LinkedHashSet<String> imageURLs = jsonParserService.extractImageURLs(inputStream);

				// Download the images and save them in temp storage
				Map<String, Path> imageFiles = imageDownloaderService.downloadImages(imageURLs);

				// Create the S3 bucket and upload the images to it.
				s3Service.createS3BucketIfNotExists();
				s3Service.uploadFileList(imageFiles);
			}

		} catch (IOException e) {
			LOGGER.error("ERROR [{}]:", this.getClass().getName(), e);
		}
	}
}
