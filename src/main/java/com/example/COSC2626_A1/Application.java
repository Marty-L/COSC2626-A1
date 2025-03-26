package com.example.COSC2626_A1;

import com.example.COSC2626_A1.service.JsonParserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private final JsonParserService jsonParserService;

	public Application(JsonParserService jsonParserService) {
		this.jsonParserService = jsonParserService;
	}

	//RUN THE MAIN SERVER APPLICATION
	public static void main(String[] args) {SpringApplication.run(Application.class, args);}

	@Override
	public void run(String... args) {
		//TODO: Fix file location (https://www.baeldung.com/spring-classpath-file-access)
		List<String> imageURLs = jsonParserService.extractImageURLs("C:/Users/mclaw/IdeaProjects/COSC2626-A1/src/main/resources/data/2025a1.json");
	}
}
