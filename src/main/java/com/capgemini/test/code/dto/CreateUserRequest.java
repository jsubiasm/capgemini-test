package com.capgemini.test.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Petición de creación de usuario")
public class CreateUserRequest
{
	@Schema(example = "pablo")
	private String name;

	@Schema(example = "email@email.com")
	private String email;

	@Schema(example = "677998899")
	private String phone;

	@Schema(example = "ADMIN")
	private String rol;

	@Schema(example = "23454234W")
	private String dni;
}