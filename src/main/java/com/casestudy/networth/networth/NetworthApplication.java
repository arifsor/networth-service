package com.casestudy.networth.networth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class NetworthApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetworthApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate(
				new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())
		);
	}
}
