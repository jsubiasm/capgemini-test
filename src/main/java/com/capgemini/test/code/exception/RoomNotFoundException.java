package com.capgemini.test.code.exception;

public class RoomNotFoundException extends RuntimeException
{

	public RoomNotFoundException(String message)
	{
		super(message);
	}
}