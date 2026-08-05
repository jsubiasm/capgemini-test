package com.capgemini.test.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest
{

	private String name;

	private String email;

	private String phone;

	private String rol;

	private String dni;
}