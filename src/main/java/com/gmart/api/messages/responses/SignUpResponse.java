package com.gmart.api.messages.responses;

import com.gmart.api.core.entities.UserCore;
import com.gmart.api.messages.responses.enums.SignUpStatus;

import lombok.Data;


@Data
public class SignUpResponse {
	CustomError error;
	SignUpStatus signUpStatus;
}
