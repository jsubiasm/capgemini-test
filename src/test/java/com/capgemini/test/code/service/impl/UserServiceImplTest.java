package com.capgemini.test.code.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capgemini.test.code.clients.DniClient;
import com.capgemini.test.code.clients.EmailClient;
import com.capgemini.test.code.clients.SmsClient;
import com.capgemini.test.code.dto.CreateUserRequest;
import com.capgemini.test.code.exception.UserAlreadyExistsException;
import com.capgemini.test.code.exception.ValidationException;
import com.capgemini.test.code.repository.RoomRepository;
import com.capgemini.test.code.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest
{
	@Mock
	UserRepository userRepository;
	@Mock
	RoomRepository roomRepository;
	@Mock
	DniClient dniClient;
	@Mock
	EmailClient emailClient;
	@Mock
	SmsClient smsClient;
	@InjectMocks
	UserServiceImpl service;

	@Test
	void shouldFailWhenNameGreaterThanSixCharacters()
	{
		CreateUserRequest r = new CreateUserRequest();
		r.setName("1234567");
		r.setEmail("a@a.com");
		r.setRol("admin");
		r.setDni("123");
		r.setPhone("666");
		assertThrows(ValidationException.class, () -> service.create(r));
	}

	@Test
	void shouldFailWhenEmailAlreadyExists()
	{
		CreateUserRequest r = new CreateUserRequest();
		r.setName("pablo");
		r.setEmail("a@a.com");
		r.setRol("admin");
		r.setDni("123");
		r.setPhone("666");
		when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(mock(com.capgemini.test.code.domain.User.class)));
		assertThrows(UserAlreadyExistsException.class, () -> service.create(r));
	}
}
