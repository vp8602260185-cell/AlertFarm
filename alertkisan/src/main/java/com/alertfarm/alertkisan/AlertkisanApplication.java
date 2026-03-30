package com.alertfarm.alertkisan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlertkisanApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertkisanApplication.class, args);
		System.out.println("AlertKisan Application Started Successfully!");
	}

}
