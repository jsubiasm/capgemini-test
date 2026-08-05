package com.capgemini.test.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Respuesta de error")
public class ErrorResponse
{
	private Integer code;
	private String message;
}