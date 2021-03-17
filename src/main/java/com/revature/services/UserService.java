package com.revature.services;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

@Service
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	public User login(String username, String password) {
		User u = findByUserName(username);
		MDC.put("event", "login");
		MDC.put("userID", Integer.toString(u.getId()));
		if(u.getPassWord().equals(password)) {
			//allow access to account
			User.setUsersLoggedOnList(u, true);
			log.info("{} logged on",  username);
			return u;
		}
		//else deny access
		log.warn("User {} failed to log on",  username);
		MDC.clear();
		throw new InvalidCredentialsException("Invalid credentials");
	}
	
	public void logout(String username) {
		User u = findByUserName(username);
		MDC.put("event", "logout");
		MDC.put("userID", Integer.toString(u.getId()));
		log.info("User {} logged out", u.getUserName());
		MDC.clear();
		User.setUsersLoggedOnList(u, false);
	}
	
	public List<User> findAll() {
		return userDAO.findAll();
	}
	
	public User findById(int id) {
		return userDAO.findById(id).orElseThrow( () -> 
			new UserNotFoundException("No user found with id " + id));
	}
	
	public User findByUserName(String username) {
		return userDAO.findByUserName(username).orElseThrow( () -> 
			new UserNotFoundException("No user found with username " + username));
	} 
	
	public User insert(User u) {
		MDC.put("event", "create");
		MDC.put("userID", Integer.toString(u.getId()));
		log.info("User {} created", u.getUserName());
		MDC.clear();
		return userDAO.save(u);
	}
	
	public String delete(int id) {
		MDC.put("event", "delete");
		MDC.put("userID", Integer.toString(id));
		log.info("User {} deleted", Integer.toString(id));
		MDC.clear();
		userDAO.deleteById(id);
		return "Deleted";
	}
}
