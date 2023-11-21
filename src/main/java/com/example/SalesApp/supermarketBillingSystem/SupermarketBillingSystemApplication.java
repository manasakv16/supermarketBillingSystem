package com.example.SalesApp.supermarketBillingSystem;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.Role;
import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import com.example.SalesApp.supermarketBillingSystem.security.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;

@SpringBootApplication
public class SupermarketBillingSystemApplication implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger(SupermarketBillingSystemApplication.class.getName());
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		System.setProperty("log4j.configurationFile", "src/main/resources/log4j2.xml");
		SpringApplication.run(SupermarketBillingSystemApplication.class, args);
		logger.info("Started application at " + LocalDateTime.now());
	}

	// create a admin account during first runtime.
	public void run(String[] args) {
		final User user = userRepository.findByRole(Role.ADMIN);
		if(user == null ){
			User newUser = new User();
			newUser.setEmail("minmin@gmail.com");
			newUser.setFirstName("Ahn Min");
			newUser.setLastName("Hyuk");
			newUser.setRole(Role.ADMIN);
			newUser.setPassword(new BCryptPasswordEncoder().encode("bongbong"));
			userRepository.save(newUser);
			logger.info("Admin account created ");
		}
		else {
			logger.info("Admin account already present ");
		}
	}

}
