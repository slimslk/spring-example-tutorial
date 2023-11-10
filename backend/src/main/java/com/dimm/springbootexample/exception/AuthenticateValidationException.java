package com.dimm.springbootexample.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AuthenticateValidationException extends RuntimeException{
	public AuthenticateValidationException(String message){
		super(message);
	}
}
