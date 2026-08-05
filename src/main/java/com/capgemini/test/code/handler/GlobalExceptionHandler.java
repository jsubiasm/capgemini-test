package com.capgemini.test.code.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.capgemini.test.code.dto.ErrorResponse;
import com.capgemini.test.code.exception.UserAlreadyExistsException;
import com.capgemini.test.code.exception.UserNotFoundException;
import com.capgemini.test.code.exception.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler
{

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ErrorResponse emailExists(UserAlreadyExistsException ex)
	{
		return new ErrorResponse(409, ex.getMessage());
	}

	@ExceptionHandler(ValidationException.class)
	public ErrorResponse validation(ValidationException ex)
	{
		return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ErrorResponse userNotFound(UserNotFoundException ex)
	{
		return new ErrorResponse(404, ex.getMessage());
	}
}