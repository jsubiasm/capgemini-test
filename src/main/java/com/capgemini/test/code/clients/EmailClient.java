package com.capgemini.test.code.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.capgemini.test.code.dto.NotificationRequest;

@FeignClient(name = "emailClient", url = "${external.service.url}")
public interface EmailClient
{

	@PostMapping("/email")
	void send(@RequestBody NotificationRequest request);

}