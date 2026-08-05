package com.capgemini.test.code.exception;

public class UserNotFoundException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2938063242876888939L;

	public UserNotFoundException(String message)
	{
		super(message);
	}

}