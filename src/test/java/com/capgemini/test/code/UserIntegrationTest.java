package com.capgemini.test.code;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest
{

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnConflictForInvalidRole() throws Exception
	{
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"pablo\",\"email\":\"a@a.com\",\"phone\":\"666\",\"rol\":\"user\",\"dni\":\"123\"}")).andExpect(status().isConflict());
	}
}
