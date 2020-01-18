package com.gmart.api.core.exceptions;

import javassist.NotFoundException;

public class UserSignInException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String code;

	public UserSignInException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
	}

	public UserSignInException(String msg, String code) {
		super(msg);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
