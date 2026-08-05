package com.capgemini.test.code.exception;

public class InvalidDniException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7719292432209312147L;

	public InvalidDniException(String message)
	{
		super(message);
	}
}
