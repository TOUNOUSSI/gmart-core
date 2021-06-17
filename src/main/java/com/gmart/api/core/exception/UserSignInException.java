package com.gmart.api.core.exception;

import javassist.NotFoundException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserSignInException extends NotFoundException {

	/**
	 *
	 */
	private static final long serialVersionUID = -5546108652118406630L;
	private String code;

	public UserSignInException(String msg) {
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
