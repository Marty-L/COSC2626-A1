package com.example.COSC2626_A1;

import com.example.COSC2626_A1.service.JsonParserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private final JsonParserService jsonParserService;

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());


	public Application(JsonParserService jsonParserService) {
		this.jsonParserService = jsonParserService;
	}

	//RUN THE MAIN SERVER APPLICATION
	public static void main(String[] args) {SpringApplication.run(Application.class, args);}

	@Override
	public void run(String... args) {
		//TODO: Fix file location (https://www.baeldung.com/spring-classpath-file-access)
		try {
			File file = new ClassPathResource("data/2025a1.json").getFile();
			String filePath = file.getAbsolutePath();
			List<String> imageURLs = jsonParserService.extractImageURLs(filePath);
		} catch (IOException e) {
			LOGGER.error("Error reading file: " + e.getMessage());
		}

	}
}
