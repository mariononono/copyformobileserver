package com.example.mjukvarukonstruktionbokningserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MjukvarukonstruktionBokningserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MjukvarukonstruktionBokningserverApplication.class, args);
	}
}
