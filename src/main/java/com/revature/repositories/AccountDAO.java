package com.revature.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.revature.models.Account;

public interface AccountDAO extends JpaRepository<Account, Integer> {

	public Optional<Account> findById(int id);
	
	public Optional<List<Account>> findByOwnerId(int ownerId);

	@Query("select balance from Account a where a.id = :accountNumber")
	public double findBalanceById(int accountNumber);
	
	@Modifying
	@Transactional
	@Query("update Account a set a.balance = (a.balance + :amount) where a.id = :accountNumber")
	public void deposit(int accountNumber, double amount);
	
	@Modifying
	@Transactional
	@Query("update Account a set a.balance = (a.balance - :amount) where a.id = :accountNumber")
	public void withdraw(int accountNumber, double amount);
	
	

}
