package com.koball.springboot.restapidemo.user;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDetailsRestRepository extends PagingAndSortingRepository<UserDetails, Integer> {

	public List<UserDetails> findByRole(String role);
	
}
