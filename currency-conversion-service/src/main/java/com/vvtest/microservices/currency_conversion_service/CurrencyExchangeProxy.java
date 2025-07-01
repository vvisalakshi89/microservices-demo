package com.vvtest.microservices.currency_conversion_service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.vvtest.microservices.currency_conversion_service.bean.CurrencyConversion;


//@FeignClient(name="currency-exchange", url="localhost:8002")
@FeignClient(name="currency-exchange")
public interface CurrencyExchangeProxy {
	
	@GetMapping("currency-exchange/from/{fromCur}/to/{toCur}")
	public CurrencyConversion getConvertedAmount(
		@PathVariable String fromCur,
		@PathVariable String toCur) ;
	
		
	
	

}
