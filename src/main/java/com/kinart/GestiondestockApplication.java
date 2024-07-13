package com.kinart;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableEncryptableProperties
@SpringBootApplication
@EnableJpaAuditing
@EnableWebMvc
public class GestiondestockApplication extends SpringBootServletInitializer {

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(GestiondestockApplication.class, args);

	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(GestiondestockApplication.class);
//	}

}
