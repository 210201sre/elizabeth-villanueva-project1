package com.revature.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data @AllArgsConstructor @NoArgsConstructor
public class User {
	
	@Length(min=1)
	@NotBlank
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]*")
	private String userName;
	
	@Length(min=1)
	@NotNull
	private String passWord;
	
	@Length(min=1)
	private String firstName;
	
	@Length(min=1)
	private String lastName;
	
	@Email
	private String email;
	
	@Id
	//@Column(name="user_id", nullable=false, unique=true, updatable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private static List<User> usersLoggedOnList = new ArrayList<>();

	public static List<User> getUsersLoggedOnList() {
		return usersLoggedOnList;
	}
	
	public static void setUsersLoggedOnList(User u, boolean b) {
		if (b) usersLoggedOnList.add(u);
		else {
			int count = 0;
			for (User user : usersLoggedOnList) {
				if(user.getId() == u.getId()) {
					usersLoggedOnList.remove(count);
					break;
				}
				count++;
			}
		}
	}		
	
}
