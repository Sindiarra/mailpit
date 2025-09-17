package net.ada.mailpit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailpitApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailpitApplication.class, args);
	}

}
