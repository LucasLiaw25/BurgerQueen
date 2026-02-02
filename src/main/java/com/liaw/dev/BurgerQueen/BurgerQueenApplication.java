package com.liaw.dev.BurgerQueen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BurgerQueenApplication {

	public static void main(String[] args) {
		SpringApplication.run(BurgerQueenApplication.class, args);
	}

}
