package com.capgemini.test.code.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.capgemini.test.code.dto.ErrorResponse;
import com.capgemini.test.code.exception.InvalidDniException;
import com.capgemini.test.code.exception.RoomNotFoundException;
import com.capgemini.test.code.exception.UserAlreadyExistsException;
import com.capgemini.test.code.exception.UserNotFoundException;
import com.capgemini.test.code.exception.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> userAlreadyExists(UserAlreadyExistsException ex)
	{
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> validation(ValidationException ex)
	{
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
	}

	@ExceptionHandler(InvalidDniException.class)
	public ResponseEntity<ErrorResponse> invalidDni(InvalidDniException ex)
	{
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> userNotFound(UserNotFoundException ex)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(RoomNotFoundException.class)
	public ResponseEntity<ErrorResponse> roomNotFound(RoomNotFoundException ex)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}
}