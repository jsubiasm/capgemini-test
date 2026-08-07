package com.capgemini.test.code.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.capgemini.test.code.dto.ErrorResponse;
import com.capgemini.test.code.exception.InvalidDniException;
import com.capgemini.test.code.exception.RoomNotFoundException;
import com.capgemini.test.code.exception.UserAlreadyExistsException;
import com.capgemini.test.code.exception.UserNotFoundException;
import com.capgemini.test.code.exception.ValidationException;

class GlobalExceptionHandlerTest
{
	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	void shouldMapUserAlreadyExistsToConflict()
	{
		ResponseEntity<ErrorResponse> response = handler.userAlreadyExists(new UserAlreadyExistsException("error validation email"));

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals(409, response.getBody().getCode());
		assertEquals("error validation email", response.getBody().getMessage());
	}

	@Test
	void shouldMapValidationToConflict()
	{
		ResponseEntity<ErrorResponse> response = handler.validation(new ValidationException("error validation userName"));

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals(409, response.getBody().getCode());
		assertEquals("error validation userName", response.getBody().getMessage());
	}

	@Test
	void shouldMapInvalidDniToConflict()
	{
		ResponseEntity<ErrorResponse> response = handler.invalidDni(new InvalidDniException("error validation dni"));

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals(409, response.getBody().getCode());
		assertEquals("error validation dni", response.getBody().getMessage());
	}

	@Test
	void shouldMapUserNotFoundToNotFound()
	{
		ResponseEntity<ErrorResponse> response = handler.userNotFound(new UserNotFoundException("User not found"));

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(404, response.getBody().getCode());
		assertEquals("User not found", response.getBody().getMessage());
	}

	@Test
	void shouldMapRoomNotFoundToNotFound()
	{
		ResponseEntity<ErrorResponse> response = handler.roomNotFound(new RoomNotFoundException("Room 1 not found"));

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(404, response.getBody().getCode());
		assertEquals("Room 1 not found", response.getBody().getMessage());
	}
}