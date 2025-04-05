package br.com.controlapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ControlApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlApiApplication.class, args);
	}

}
