package com.vvtest.microservices.currency_exchange_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;


@RestController
public class CircuitBreakerController {

	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	@GetMapping("/sample-api")
//	@Retry(name = "sample-api", fallbackMethod = "fallbackMethodResponse")
	@CircuitBreaker(name="default",fallbackMethod = "fallbackMethodResponse")
	public String simpleApi()
	{
		
		logger.info("Sample call retry attempt...");
		ResponseEntity<String> responseEntity = 
				new RestTemplate().getForEntity("http://localhost:8001/test", String.class);
		
		return responseEntity.getBody();
	}
	
	public String fallbackMethodResponse(Exception e)
	{
		return "Error Occured from Cir Brk";
		
	}
}
