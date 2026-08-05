package com.capgemini.test.code.service;

import com.capgemini.test.code.dto.CreateUserRequest;
import com.capgemini.test.code.dto.CreateUserResponse;
import com.capgemini.test.code.dto.UserResponse;

public interface UserService
{

	CreateUserResponse create(CreateUserRequest request);

	UserResponse get(Long id);

}