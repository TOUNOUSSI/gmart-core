package com.gmart.api.messages.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmart.api.messages.responses.enums.LoginStatus;

import lombok.Data;

@Data
public class SignInResponse {
	@JsonProperty("authenticatedUser")
	UserInfo authenticatedUser;
	
	@JsonProperty("error")
	CustomError error;
	
	@JsonProperty("loginStatus")
	LoginStatus loginStatus;
	
	@JsonProperty("token")
	String token;
}
