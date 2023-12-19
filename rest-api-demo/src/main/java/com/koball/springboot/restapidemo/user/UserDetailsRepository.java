package com.koball.springboot.restapidemo.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

	public List<UserDetails> findByRole(String role);
	
}
