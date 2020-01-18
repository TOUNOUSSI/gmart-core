package com.gmart.api.messages.responses;

import lombok.Data;

@Data
public class CustomError {

	String code;
	String message;
}
