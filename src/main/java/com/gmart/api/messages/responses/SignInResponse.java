package com.gmart.api.messages.responses;

import com.gmart.api.messages.responses.enums.LoginStatus;

import lombok.Data;

@Data
public class SignInResponse {
	UserInfo authenticatedUser;
	CustomError error;
	LoginStatus loginStatus;
	String token;
}
