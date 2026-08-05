package com.capgemini.test.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Respuesta de creación")
public class CreateUserResponse
{
	@Schema(example = "1")
	private Long id;
}