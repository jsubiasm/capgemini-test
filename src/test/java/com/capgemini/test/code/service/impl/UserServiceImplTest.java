package com.capgemini.test.code.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capgemini.test.code.clients.CheckDniResponse;
import com.capgemini.test.code.clients.DniClient;
import com.capgemini.test.code.clients.EmailClient;
import com.capgemini.test.code.clients.SmsClient;
import com.capgemini.test.code.domain.Role;
import com.capgemini.test.code.domain.Room;
import com.capgemini.test.code.domain.User;
import com.capgemini.test.code.dto.CreateUserRequest;
import com.capgemini.test.code.dto.CreateUserResponse;
import com.capgemini.test.code.dto.NotificationRequest;
import com.capgemini.test.code.dto.UserResponse;
import com.capgemini.test.code.exception.InvalidDniException;
import com.capgemini.test.code.exception.UserAlreadyExistsException;
import com.capgemini.test.code.exception.UserNotFoundException;
import com.capgemini.test.code.exception.ValidationException;
import com.capgemini.test.code.repository.RoomRepository;
import com.capgemini.test.code.repository.UserRepository;

import feign.Request;
import feign.Response;

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
	void shouldCreateAdminUserAndSendEmail()
	{
		CreateUserRequest request = validRequest("admin");
		Room room = room(1L);
		User savedUser = user(10L, request, room, Role.ADMIN);

		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
		when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		when(dniClient.check(any())).thenReturn(org.springframework.http.ResponseEntity.ok(new CheckDniResponse("Valid DNI")));

		CreateUserResponse response = service.create(request);

		assertEquals(10L, response.getId());

		ArgumentCaptor<NotificationRequest> notificationCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
		verify(emailClient).send(notificationCaptor.capture());
		verify(smsClient, never()).send(any());
		assertEquals("usuario guardado", notificationCaptor.getValue().getMessage());
		assertEquals(request.getEmail(), notificationCaptor.getValue().getEmail());
		assertEquals(request.getPhone(), notificationCaptor.getValue().getPhone());
	}

	@Test
	void shouldCreateSuperadminUserAndSendSms()
	{
		CreateUserRequest request = validRequest("superadmin");
		Room room = room(1L);
		User savedUser = user(11L, request, room, Role.SUPERADMIN);

		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
		when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		when(dniClient.check(any())).thenReturn(org.springframework.http.ResponseEntity.ok(new CheckDniResponse("Valid DNI")));

		CreateUserResponse response = service.create(request);

		assertEquals(11L, response.getId());
		verify(smsClient).send(any(NotificationRequest.class));
		verify(emailClient, never()).send(any());
	}

	@Test
	void shouldFailWhenRequestIsNull()
	{
		assertThrows(ValidationException.class, () -> service.create(null));
	}

	@Test
	void shouldFailWhenNameGreaterThanSixCharacters()
	{
		assertThrows(ValidationException.class, () -> service.create(requestWithName("1234567")));
	}

	@Test
	void shouldFailWhenEmailIsInvalid()
	{
		assertThrows(ValidationException.class, () -> service.create(requestWithEmail("invalid-email")));
	}

	@Test
	void shouldFailWhenRoleIsInvalid()
	{
		assertThrows(ValidationException.class, () -> service.create(requestWithRole("user")));
	}

	@Test
	void shouldFailWhenDniIsMissing()
	{
		assertThrows(ValidationException.class, () -> service.create(requestWithDni("")));
	}

	@Test
	void shouldFailWhenPhoneIsMissing()
	{
		assertThrows(ValidationException.class, () -> service.create(requestWithPhone("")));
	}

	@Test
	void shouldFailWhenEmailAlreadyExists()
	{
		CreateUserRequest request = validRequest("ADMIN");
		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

		assertThrows(UserAlreadyExistsException.class, () -> service.create(request));
	}

	@Test
	void shouldFailWhenDniIsRejectedByExternalService()
	{
		CreateUserRequest request = validRequest("ADMIN");

		when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
		when(dniClient.check(any())).thenThrow(feign.FeignException.errorStatus("check", conflictResponse()));

		assertThrows(InvalidDniException.class, () -> service.create(request));
		verify(userRepository, never()).save(any());
	}

	@Test
	void shouldReturnUserById()
	{
		User user = new User();
		user.setId(5L);
		user.setName("pablo");
		user.setEmail("a@a.com");
		user.setPhone("666");
		user.setDni("123");
		user.setRole(Role.ADMIN);

		when(userRepository.findById(5L)).thenReturn(Optional.of(user));

		UserResponse response = service.get(5L);

		assertEquals(5L, response.getId());
		assertEquals("pablo", response.getName());
		assertEquals("a@a.com", response.getEmail());
		assertEquals("666", response.getPhone());
		assertEquals("123", response.getDni());
		assertEquals("ADMIN", response.getRole());
	}

	@Test
	void shouldFailWhenUserDoesNotExist()
	{
		when(userRepository.findById(5L)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> service.get(5L));
	}

	private CreateUserRequest validRequest(String rol)
	{
		CreateUserRequest request = new CreateUserRequest();
		request.setName("pablo");
		request.setEmail("pablo@example.com");
		request.setPhone("677998899");
		request.setRol(rol);
		request.setDni("23454234W");
		return request;
	}

	private CreateUserRequest requestWithName(String name)
	{
		CreateUserRequest request = validRequest("ADMIN");
		request.setName(name);
		return request;
	}

	private CreateUserRequest requestWithEmail(String email)
	{
		CreateUserRequest request = validRequest("ADMIN");
		request.setEmail(email);
		return request;
	}

	private CreateUserRequest requestWithRole(String role)
	{
		CreateUserRequest request = validRequest(role);
		return request;
	}

	private CreateUserRequest requestWithDni(String dni)
	{
		CreateUserRequest request = validRequest("ADMIN");
		request.setDni(dni);
		return request;
	}

	private CreateUserRequest requestWithPhone(String phone)
	{
		CreateUserRequest request = validRequest("ADMIN");
		request.setPhone(phone);
		return request;
	}

	private Room room(Long id)
	{
		Room room = new Room();
		room.setId(id);
		room.setName("Sala 1");
		return room;
	}

	private User user(Long id, CreateUserRequest request, Room room, Role role)
	{
		User user = new User();
		user.setId(id);
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setDni(request.getDni());
		user.setRole(role);
		user.setRoom(room);
		return user;
	}

	private Response conflictResponse()
	{
		Request request = Request.create(Request.HttpMethod.PATCH, "http://localhost/check-dni", java.util.Collections.emptyMap(), null, StandardCharsets.UTF_8, null);
		return Response.builder().status(409).reason("Conflict").request(request).headers(java.util.Collections.emptyMap()).body("{}", StandardCharsets.UTF_8).build();
	}
}
