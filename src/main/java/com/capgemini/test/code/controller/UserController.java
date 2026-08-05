package com.capgemini.test.code.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.test.code.dto.CreateUserRequest;
import com.capgemini.test.code.dto.CreateUserResponse;
import com.capgemini.test.code.dto.UserResponse;
import com.capgemini.test.code.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController
{

	private final UserService userService;

	@PostMapping
	public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request)
	{

		return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
	}

	@GetMapping("/{id}")
	public UserResponse get(@PathVariable Long id)
	{
		return userService.get(id);
	}
}