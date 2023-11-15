package com.dimm.springbootexample.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Date;

@ControllerAdvice
public class DefaultExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFoundException (
			ResourceNotFoundException ex,
			HttpServletRequest request
	) {
		ApiError error = new ApiError(
				request.getRequestURI(),
				ex.getMessage(),
				HttpStatus.NOT_FOUND.value(),
				Date.from(Instant.now()));
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RequestValidationException.class)
	public ResponseEntity<ApiError> handleRequestValidationException (
			RequestValidationException ex,
			HttpServletRequest request
	) {
		ApiError error = new ApiError(
				request.getRequestURI(),
				ex.getMessage(),
				HttpStatus.BAD_REQUEST.value(),
				Date.from(Instant.now()));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ApiError> handleDuplicateResourceException (
			DuplicateResourceException ex,
			HttpServletRequest request
	) {
		ApiError error = new ApiError(
				request.getRequestURI(),
				ex.getMessage(),
				HttpStatus.CONFLICT.value(),
				Date.from(Instant.now()));
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<ApiError> handleDuplicateResourceException (
			InsufficientAuthenticationException ex,
			HttpServletRequest request
	) {
		ApiError error = new ApiError(
				request.getRequestURI(),
				ex.getMessage(),
				HttpStatus.FORBIDDEN.value(),
				Date.from(Instant.now()));
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException (
			Exception ex,
			HttpServletRequest request
	) {
		ApiError error = new ApiError(
				request.getRequestURI(),
				ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				Date.from(Instant.now()));
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> badCredentialException (
			BadCredentialsException ex,
			HttpServletRequest request
	) {
		ApiError error = new ApiError(
				request.getRequestURI(),
				ex.getMessage(),
				HttpStatus.UNAUTHORIZED.value(),
				Date.from(Instant.now()));
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
}
