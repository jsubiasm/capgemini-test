package com.capgemini.test.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Información de usuario")
public class UserResponse
{
	private Long id;
	private String name;
	private String email;
	private String phone;
	private String dni;
	private String role;
}