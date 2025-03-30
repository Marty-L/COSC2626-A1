package com.example.COSC2626_A1;

import com.example.COSC2626_A1.service.ImageDownloaderService;
import com.example.COSC2626_A1.service.JsonParserService;

import com.example.COSC2626_A1.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
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

	//TODO: implement global logging/exception handling
	//private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());

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
			//Convert the classpath reference to an absolute path for the local filesystem
			//Adapted from: https://www.baeldung.com/spring-classpath-file-access
			//Viewed: 2025-03-06
			File file = new ClassPathResource("data/2025a1.json").getFile();
			String filePath = file.getAbsolutePath();

			//Parse the JSON file and grab the imageURLs for downloading from GitHub
			LinkedHashSet<String> imageURLs = jsonParserService.extractImageURLs(filePath);

			//Download the images and save them in temp storage
			Map<String, Path> imageFiles = imageDownloaderService.downloadImages(imageURLs);

			//Create the S3 bucket and upload the images to it.
			s3Service.createS3BucketIfNotExists();
			s3Service.uploadFile("TaylorSwift.jpg", imageFiles.get("TaylorSwift.jpg"));

		} catch (IOException e) {
			//TODO: Add better exception handling
			e.printStackTrace();
		}
	}
}
