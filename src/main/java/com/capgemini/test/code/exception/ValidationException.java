package com.capgemini.test.code.exception;

public class ValidationException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3493916822340953772L;

	public ValidationException(String message)
	{
		super(message);
	}

}