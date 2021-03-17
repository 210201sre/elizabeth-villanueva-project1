package com.revature.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.exceptions.InvalidAmountException;
import com.revature.exceptions.NotLoggedOnException;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.AccountService;
import com.revature.services.UserService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<List<Account>> findAll() {
		List<Account> allAccounts = accountService.findAll();
		if(allAccounts.isEmpty()) {
			ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allAccounts);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Account> findById(@PathVariable(name="id") int id) {
		return ResponseEntity.ok(accountService.findById(id));
	}
	
	@GetMapping("/owner/{id}")
	public ResponseEntity<List<Account>> findByOwnerId(@PathVariable(name="id") int id) {
		return ResponseEntity.ok(accountService.findByOwnerId(id));
	}
	
	@PostMapping
	public ResponseEntity<Account> insert(@RequestBody Account a) {
		a.setBalance(0.00);
		a.setId(0);
		return ResponseEntity.accepted().body(accountService.insert(a));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable(name="id") int id) {
		Account a = accountService.findById(id);
		int ownerId = a.getOwnerId();
		List<User> loggedOnList = User.getUsersLoggedOnList();
		boolean found = false;
		for(User u : loggedOnList) {
			if(u.getId() == ownerId) {
				found = true;
				break;
			}
		}
		if(!found) throw new NotLoggedOnException("Account owner is not logged on");
		return ResponseEntity.ok(accountService.delete(id));
	}
	
	@GetMapping("/{id}/deposit/{amount}")
	public ResponseEntity<Double> deposit(@PathVariable(name="id") int id, 
			@PathVariable(name="amount") double amount) {
		if(amount < 0) throw new InvalidAmountException("Amount cannot be negative");
		Account a = accountService.findById(id);
		int ownerId = a.getOwnerId();
		List<User> loggedOnList = User.getUsersLoggedOnList();
		boolean found = false;
		for(User u : loggedOnList) {
			if(u.getId() == ownerId) {
				found = true;
				break;
			}
		}
		if(!found) throw new NotLoggedOnException("Account owner is not logged on");
		accountService.deposit(id, amount);
		return ResponseEntity.ok(accountService.findBalanceById(id));
	}
	
	@GetMapping("/{id}/withdraw/{amount}")
	public ResponseEntity<Double> withdraw(@PathVariable(name="id") int id, 
			@PathVariable(name="amount") double amount) {
		if(amount < 0) throw new InvalidAmountException("Amount cannot be negative");
		Account a = accountService.findById(id);
		if(a.getBalance() - amount < 0) throw new InvalidAmountException("Insufficient funds");
		int ownerId = a.getOwnerId();
		List<User> loggedOnList = User.getUsersLoggedOnList();
		boolean found = false;
		for(User u : loggedOnList) {
			if(u.getId() == ownerId) {
				found = true;
				break;
			}
		}
		if(!found) throw new NotLoggedOnException("Account owner is not logged on");
		accountService.withdraw(id, amount);
		return ResponseEntity.ok(accountService.findBalanceById(id));
	}
	
	@GetMapping("/{id1}/transfer/{id2}/{amount}")
	public ResponseEntity<List<Double>> withdraw(@PathVariable(name="id1") int id1, 
			@PathVariable(name="id2") int id2, @PathVariable(name="amount") double amount) {
		if(amount < 0) throw new InvalidAmountException("Amount cannot be negative");
		Account a = accountService.findById(id1);
		int ownerId = a.getOwnerId();
		List<User> loggedOnList = User.getUsersLoggedOnList();
		boolean found = false;
		for(User u : loggedOnList) {
			if(u.getId() == ownerId) {
				found = true;
				break;
			}
		}
		if(!found) throw new NotLoggedOnException("Account owner is not logged on");
		accountService.withdraw(id1, amount);
		accountService.deposit(id2, amount);
		List<Double> balances = new ArrayList<Double>();
		balances.add(accountService.findBalanceById(id1));
		balances.add(accountService.findBalanceById(id2));
		return ResponseEntity.ok(balances);
	}
}
