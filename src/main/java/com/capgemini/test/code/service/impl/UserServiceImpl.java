package com.capgemini.test.code.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.test.code.clients.CheckDniRequest;
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
import com.capgemini.test.code.exception.RoomNotFoundException;
import com.capgemini.test.code.exception.UserAlreadyExistsException;
import com.capgemini.test.code.exception.UserNotFoundException;
import com.capgemini.test.code.exception.ValidationException;
import com.capgemini.test.code.repository.RoomRepository;
import com.capgemini.test.code.repository.UserRepository;
import com.capgemini.test.code.service.UserService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final DniClient dniClient;
	private final EmailClient emailClient;
	private final SmsClient smsClient;

	@Override
	public CreateUserResponse create(CreateUserRequest request)
	{
		validateRequest(request);

		if (userRepository.findByEmail(request.getEmail()).isPresent())
		{
			throw new UserAlreadyExistsException("error validation email");
		}

		validateDni(request.getDni());

		Room room = roomRepository.findById(1L).orElseThrow(() -> new RoomNotFoundException("Room 1 not found"));

		User user = mapToUser(request, room);

		User savedUser = userRepository.save(user);

		notifyUser(savedUser);

		return new CreateUserResponse(savedUser.getId());
	}

	@Override
	public UserResponse get(Long id)
	{
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getDni(), user.getRole().name());
	}

	private void validateRequest(CreateUserRequest request)
	{
		if (request == null)
		{
			throw new ValidationException("invalid request");
		}

		if (request.getName() == null || request.getName().isBlank() || request.getName().length() > 6)
		{
			throw new ValidationException("error validation userName");
		}

		if (request.getEmail() == null || !request.getEmail().contains("@") || !request.getEmail().contains("."))
		{
			throw new ValidationException("error validation email");
		}

		if (request.getRol() == null || request.getRol().isBlank())
		{
			throw new ValidationException("error validation rol");
		}

		if (!"admin".equalsIgnoreCase(request.getRol()) && !"superadmin".equalsIgnoreCase(request.getRol()))
		{
			throw new ValidationException("error validation rol");
		}

		if (request.getDni() == null || request.getDni().isBlank())
		{
			throw new ValidationException("error validation dni");
		}

		if (request.getPhone() == null || request.getPhone().isBlank())
		{
			throw new ValidationException("error validation phone");
		}
	}

	private void validateDni(String dni)
	{
		try
		{
			dniClient.check(new CheckDniRequest(dni));
		}
		catch (FeignException ex)
		{
			if (ex.status() == 409)
			{
				throw new InvalidDniException("error validation dni");
			}

			throw new ValidationException("External DNI service error");
		}
	}

	private User mapToUser(CreateUserRequest request, Room room)
	{
		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setDni(request.getDni());
		user.setRole(Role.valueOf(request.getRol().toUpperCase()));
		user.setRoom(room);

		return user;
	}

	private void notifyUser(User user)
	{
		NotificationRequest notification = new NotificationRequest("usuario guardado", user.getEmail(), user.getPhone());

		if (Role.ADMIN.equals(user.getRole()))
		{
			emailClient.send(notification);
		}
		else
		{
			smsClient.send(notification);
		}
	}
}
