package com.gmart.api.messages.responses;

import java.util.Set;

import com.gmart.api.core.entities.Role;

import lombok.Data;

@Data
public class UserInfo {

	private String email;
	private String firstname;
	private Long id;
	private String lastname;
	private String phone;
	private Set<Role> roles;
	private String username;
}
