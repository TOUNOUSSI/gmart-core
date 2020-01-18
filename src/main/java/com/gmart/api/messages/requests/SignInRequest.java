package com.gmart.api.messages.requests;

import lombok.Data;

@Data
public class SignInRequest {
	private String password;
	private String username;
}
