package com.backend.sade;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAsync // Asenkron işlemler için gerekli
@EnableScheduling // Zamanlanmış görevler için gerekli
public class SadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SadeApplication.class, args);
	}
	// RestTemplate Bean Tanımlaması (API Çağrıları İçin)
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// ModelMapper Bean Tanımlaması (DTO ve Entity Dönüşümleri İçin)
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
