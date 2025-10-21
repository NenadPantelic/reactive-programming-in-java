package com.np.playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

// read `sec` from the application properties file
@SpringBootApplication(scanBasePackages = "com.np.playground.${sec}")
@EnableR2dbcRepositories(basePackages = "com.np.playground.${sec}")
public class WebfluxPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxPlaygroundApplication.class, args);
	}

}
