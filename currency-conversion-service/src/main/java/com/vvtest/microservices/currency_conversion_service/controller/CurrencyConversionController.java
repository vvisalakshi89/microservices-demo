package com.vvtest.microservices.currency_conversion_service.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.vvtest.microservices.currency_conversion_service.CurrencyExchangeProxy;
import com.vvtest.microservices.currency_conversion_service.bean.CurrencyConversion;

@RestController
public class CurrencyConversionController {

	@Autowired
	CurrencyExchangeProxy proxy;
	//http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/10
	
	@GetMapping("currency-conversion/from/{fromCur}/to/{toCur}/quantity/{quantity}")
	public CurrencyConversion getCurrencyConversion(@PathVariable String fromCur, 
			@PathVariable String toCur, @PathVariable BigDecimal quantity) {
		
		HashMap<String, String> urlParams = new HashMap<>();
		urlParams.put("fromCur", fromCur);
		urlParams.put("toCur", toCur);
		
		ResponseEntity<CurrencyConversion> response= 
				new RestTemplate().getForEntity("http://localhost:8002/currency-exchange/from/{fromCur}/to/{toCur}", 
						CurrencyConversion.class,urlParams);
		
		CurrencyConversion currencyConversion = response.getBody();
		
		return new CurrencyConversion(currencyConversion.getId(),
				fromCur, toCur ,quantity, 
				currencyConversion.getConversionMultiple(),
				quantity.multiply(currencyConversion.getConversionMultiple()),
				currencyConversion.getEnvironment());
	}
	
	

	@GetMapping("currency-conversion-feign/from/{fromCur}/to/{toCur}/quantity/{quantity}")
	public CurrencyConversion getCurrencyConversionFeign(@PathVariable String fromCur, 
			@PathVariable String toCur, @PathVariable BigDecimal quantity) {
		
		HashMap<String, String> urlParams = new HashMap<>();
		urlParams.put("fromCur", fromCur);
		urlParams.put("toCur", toCur);
		
		
		CurrencyConversion currencyConversion = proxy.getConvertedAmount(fromCur, toCur);
		
		/*ResponseEntity<CurrencyConversion> response= 
				new RestTemplate().getForEntity("http://localhost:8002/currency-exchange/from/{fromCur}/to/{toCur}", 
						CurrencyConversion.class,urlParams);
		
		CurrencyConversion currencyConversion = response.getBody();*/
		
		return new CurrencyConversion(currencyConversion.getId(),
				fromCur, toCur ,quantity, 
				currencyConversion.getConversionMultiple(),
				quantity.multiply(currencyConversion.getConversionMultiple()),
				currencyConversion.getEnvironment()+"feign");
	}
}
