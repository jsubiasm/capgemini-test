package com.capgemini.test.code.exception;

public class UserAlreadyExistsException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5789464738066229548L;

	public UserAlreadyExistsException(String message)
	{
		super(message);
	}

}