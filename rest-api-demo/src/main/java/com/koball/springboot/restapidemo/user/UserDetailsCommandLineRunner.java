package com.koball.springboot.restapidemo.user;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	private UserDetailsRepository repository;
	
	public UserDetailsCommandLineRunner(UserDetailsRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void run(String... args) throws Exception {
		logger.info(Arrays.toString(args));
		
		repository.save(new UserDetails("admin", "ADMIN"));
		repository.save(new UserDetails("xande", "ADMIN"));
		repository.save(new UserDetails("josi", "ADMIN"));
		
		List<UserDetails> allUsers = repository.findAll();
		
		allUsers.forEach(u -> logger.info(u.toString()));
		
	}
	
}
