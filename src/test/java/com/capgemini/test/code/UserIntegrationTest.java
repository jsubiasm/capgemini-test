package com.capgemini.test.code;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.test.code.clients.CheckDniResponse;
import com.capgemini.test.code.clients.DniClient;
import com.capgemini.test.code.clients.EmailClient;
import com.capgemini.test.code.clients.SmsClient;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DniClient dniClient;

	@MockBean
	private EmailClient emailClient;

	@MockBean
	private SmsClient smsClient;

	@Test
	void shouldReturnConflictForInvalidRole() throws Exception
	{
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"pablo\",\"email\":\"a@a.com\",\"phone\":\"666\",\"rol\":\"user\",\"dni\":\"123\"}"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.code").value(409))
				.andExpect(jsonPath("$.message").value("error validation rol"));
	}

	@Test
	void shouldCreateAndRetrieveUser() throws Exception
	{
		org.mockito.Mockito.when(dniClient.check(org.mockito.ArgumentMatchers.any())).thenReturn(ResponseEntity.ok(new CheckDniResponse("Valid DNI")));

		String email = "user." + System.currentTimeMillis() + "@example.com";
		String dni = "23" + (System.currentTimeMillis() % 10000000000L) + "W";
		String phone = "6" + (System.currentTimeMillis() % 1000000000L);
		String body = "{\"name\":\"pablo\",\"email\":\"" + email + "\",\"phone\":\"" + phone + "\",\"rol\":\"ADMIN\",\"dni\":\"" + dni + "\"}";

		String content = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Long id = Long.valueOf(content.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));

		mockMvc.perform(get("/users/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.intValue()))
				.andExpect(jsonPath("$.name").value("pablo"))
				.andExpect(jsonPath("$.email").value(email))
				.andExpect(jsonPath("$.phone").value(phone))
				.andExpect(jsonPath("$.dni").value(dni))
				.andExpect(jsonPath("$.role").value("ADMIN"));
	}
}
