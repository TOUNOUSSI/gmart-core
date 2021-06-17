package com.gmart.api.core.exception;

import lombok.Data;

@Data
public class UserSignUpException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	
	public UserSignUpException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
	}

	public UserSignUpException(String msg, String code) {
		super(msg);
		this.code = code;
	}

	
}
