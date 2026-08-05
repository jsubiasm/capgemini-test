package com.capgemini.test.code.exception;

public class UserAlreadyExistsException extends RuntimeException
{

	public UserAlreadyExistsException(String message)
	{
		super(message);
	}

}