package com.revature.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.revature.models.Account;
import com.revature.models.User;

public interface UserDAO extends JpaRepository<User, Integer> {
	
	public Optional<User> findById(int id);
	
	public Optional<User> findByUserName(String username);
	
}
