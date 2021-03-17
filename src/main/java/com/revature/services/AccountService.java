package com.revature.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.AccountNotFoundException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.repositories.AccountDAO;
import com.revature.repositories.UserDAO;

@Service
public class AccountService {

	@Autowired
	private AccountDAO accountDAO;
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	public List<Account> findAll() {
		return accountDAO.findAll();
	}
	
	public Account findById(int id) {
		return accountDAO.findById(id).orElseThrow( () -> 
			new AccountNotFoundException("No account found with id " + id));
	}
	
	public List<Account> findByOwnerId(int id) {
		return accountDAO.findByOwnerId(id).orElseThrow( () -> 
			new UserNotFoundException("No user found with id " + id));
	}
	
	public double findBalanceById(int id) {
		return accountDAO.findBalanceById(id);
	}
	
	public Account insert(Account a) {
		MDC.put("event", "create");
		MDC.put("userID", Integer.toString(a.getOwnerId()));
		MDC.put("accountID", Integer.toString(a.getId()));
		log.info("Account {} created", Integer.toString(a.getId()));
		MDC.clear();
		return accountDAO.save(a);
	}
	
	public String delete(int id) {
		MDC.put("event", "delete");
		Account a = findById(id);
		MDC.put("userID", Integer.toString(a.getOwnerId()));
		MDC.put("accountID", Integer.toString(id));
		log.info("Account {} deleted", Integer.toString(id));
		MDC.clear();
		accountDAO.deleteById(id);
		return "Deleted";
	}
	
	public void deposit(int id, double amount) {
		MDC.put("event", "deposit");
		Account a = findById(id);
		MDC.put("userID", Integer.toString(a.getOwnerId()));
		MDC.put("accountID", Integer.toString(id));
		log.info("Deposited {} into account {}", Double.toString(amount), Integer.toString(id));
		MDC.clear();
		accountDAO.deposit(id, amount);
	}
	
	public void withdraw(int id, double amount) {
		MDC.put("event", "withdraw");
		Account a = findById(id);
		MDC.put("userID", Integer.toString(a.getOwnerId()));
		MDC.put("accountID", Integer.toString(id));
		log.info("Withdrew {} from account {}", Double.toString(amount), Integer.toString(id));
		MDC.clear();
		accountDAO.withdraw(id, amount);
	}
	
}
