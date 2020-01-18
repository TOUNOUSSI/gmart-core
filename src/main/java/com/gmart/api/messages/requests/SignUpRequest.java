package com.gmart.api.messages.requests;

import lombok.Data;

@Data
public class SignUpRequest {
	String username;
	String firstname;
	String lastname;
	String phone;
	String email;
	String password;
	String passwordConfirm;


}
