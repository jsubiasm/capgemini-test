package com.capgemini.test.code.exception;

public class RoomNotFoundException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -631216327782555955L;

	public RoomNotFoundException(String message)
	{
		super(message);
	}
}