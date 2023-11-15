package com.example.SalesApp.supermarketBillingSystem;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.Role;
import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import com.example.SalesApp.supermarketBillingSystem.security.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SupermarketBillingSystemApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SupermarketBillingSystemApplication.class, args);
	}

	// create a admin account during first runtime.
	public void run(String... args) {
		User user = userRepository.findByRole(Role.ADMIN);
		if(user == null ){
			User newUser = new User();
			newUser.setEmail("minmin@gmail.com");
			newUser.setFirstName("Ahn Min");
			newUser.setLastName("Hyuk");
			newUser.setRole(Role.ADMIN);
			newUser.setPassword(new BCryptPasswordEncoder().encode("bongbong"));
			userRepository.save(newUser);
		}
	}

}
