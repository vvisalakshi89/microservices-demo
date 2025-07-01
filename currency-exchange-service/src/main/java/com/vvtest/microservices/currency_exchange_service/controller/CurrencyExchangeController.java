package com.vvtest.microservices.currency_exchange_service.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.vvtest.microservices.currency_exchange_service.bean.CurrencyExchange;
import com.vvtest.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;

@RestController
public class CurrencyExchangeController {

	@Autowired
	private CurrencyExchangeRepository repo;

@Autowired
private Environment environment;


	@GetMapping("currency-exchange/from/{fromCur}/to/{toCur}")
	public CurrencyExchange getConvertedAmount(
		@PathVariable String fromCur,
		@PathVariable String toCur) {
		
		String port = environment.getProperty("local.server.port");
		
		
	//CurrencyExchange currencyExchange = new CurrencyExchange(1000L, fromCur, toCur, 
		//			BigDecimal.valueOf(50));
	

		CurrencyExchange currencyExchange = repo.findByFromAndTo(fromCur, toCur);
	currencyExchange.setEnvironment(port);
	return currencyExchange;
		
	}
}
