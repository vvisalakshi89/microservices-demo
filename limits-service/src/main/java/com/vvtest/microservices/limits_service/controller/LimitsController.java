package com.vvtest.microservices.limits_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vvtest.microservices.limits_service.bean.Limits;
import com.vvtest.microservices.limits_service.configuration.Configuration;

@RestController
public class LimitsController {

	
	@Autowired
	Configuration config ;
	
	
	
	@GetMapping("/limits")
	public Limits retrieveLimits() {
		//return new Limits(20, 30);
		
		return new Limits(config.getMinimum(), config.getMaximum());
		
	}
	
	
}
