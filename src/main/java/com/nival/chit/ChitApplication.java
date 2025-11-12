package com.nival.chit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ChitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChitApplication.class, args);
	}

}
