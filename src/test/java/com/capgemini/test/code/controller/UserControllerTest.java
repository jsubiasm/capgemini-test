package com.capgemini.test.code.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.test.code.dto.CreateUserResponse;
import com.capgemini.test.code.dto.UserResponse;
import com.capgemini.test.code.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest
{
	@Autowired
	MockMvc mockMvc;
	@MockBean
	UserService userService;

	@Test
	void shouldGetUser() throws Exception
	{
		when(userService.get(1L)).thenReturn(new UserResponse(1L, "pablo", "a@a.com", "666", "123", "ADMIN"));
		mockMvc.perform(get("/users/1")).andExpect(status().isOk());
	}

	@Test
	void shouldCreateUser() throws Exception
	{
		when(userService.create(org.mockito.ArgumentMatchers.any())).thenReturn(new CreateUserResponse(1L));

		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"pablo\",\"email\":\"a@a.com\",\"phone\":\"666\",\"rol\":\"user\",\"dni\":\"123\"}")).andExpect(status().isCreated());
	}
}
