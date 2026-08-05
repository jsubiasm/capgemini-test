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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Usuarios", description = "Operaciones de gestión de usuarios")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController
{

	private final UserService userService;

	@Operation(summary = "Crear usuario", description = "Guarda un usuario en la sala 1 y devuelve su identificador")
	@ApiResponse(responseCode = "201", description = "Usuario creado correctamente")
	@ApiResponse(responseCode = "409", description = "Error de validación")
	@PostMapping
	public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
	}

	@Operation(summary = "Obtener usuario", description = "Obtiene un usuario por identificador")
	@ApiResponse(responseCode = "200", description = "Usuario encontrado")
	@ApiResponse(responseCode = "404", description = "Usuario no encontrado")
	@GetMapping("/{id}")
	public UserResponse get(@PathVariable Long id)
	{
		return userService.get(id);
	}
}