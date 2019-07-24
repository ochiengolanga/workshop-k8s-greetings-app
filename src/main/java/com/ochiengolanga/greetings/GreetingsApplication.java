package com.ochiengolanga.greetings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@SpringBootApplication
public class GreetingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreetingsApplication.class, args);
	}

	@RestController
	@RequestMapping("/")
	public class GreetingsController {

		@Value("${greetings.salutation}")
		private String salutation;

		@GetMapping
		public String sayHello() {
			return "V1: " + salutation + " The date today is " + new Date();
		}
	}

}