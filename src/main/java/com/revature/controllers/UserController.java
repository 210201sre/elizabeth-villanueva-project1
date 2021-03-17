package com.revature.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.exceptions.NotLoggedOnException;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.UserService;

/*@Controller*/
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/logon")
	public ResponseEntity<User> login(@RequestBody String string) {
		String[] strings = string.split("\"");
		String username = null;
		String password = null;
		for(int i = 0; i < strings.length; ++i) {
			if(strings[i].equals("userName")) {
				i += 2;
				username = strings[i];
			}
			if(strings[i].equals("passWord")) {
				i += 2;
				password = strings[i];
			}
		}
		return ResponseEntity.ok(userService.login(username, password));
	}
	
	@GetMapping("/{username}/logout")
	public ResponseEntity<String> logout(@PathVariable(name="username") String username) {
		userService.logout(username);
		return ResponseEntity.ok("Logged out");
	}
	
	@GetMapping
	public ResponseEntity<List<User>> findAll() {
		List<User> allUsers = userService.findAll();
		if(allUsers.isEmpty()) {
			ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allUsers);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable(name="id") int id) {
		return ResponseEntity.ok(userService.findById(id));
	}
	
	
	@PostMapping
	public ResponseEntity<User> insert(@RequestBody User u) {
		u.setId(0);
		return ResponseEntity.accepted().body(userService.insert(u));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable(name="id") int id) {
		List<User> loggedOnList = User.getUsersLoggedOnList();
		boolean found = false;
		for(User u : loggedOnList) {
			if(u.getId() == id) {
				found = true;
				User.setUsersLoggedOnList(u, false);
				break;
			}
		}
		if(!found) throw new NotLoggedOnException("User is not logged on");
		return ResponseEntity.ok(userService.delete(id));
	}
}
