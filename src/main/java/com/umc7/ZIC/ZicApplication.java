package com.umc7.ZIC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZicApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZicApplication.class, args);
	}

}
