package br.com.douglas.oracao24h;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Oracao24hApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oracao24hApplication.class, args);

		System.out.println(System.getenv("AWS_ACCESS_KEY_ID"));
		System.out.println(System.getenv("AWS_SECRET_ACCESS_KEY"));
	}

}
