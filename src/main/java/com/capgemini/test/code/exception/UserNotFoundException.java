package com.capgemini.test.code.exception;

public class UserNotFoundException extends RuntimeException
{

	public UserNotFoundException(String message)
	{
		super(message);
	}

}