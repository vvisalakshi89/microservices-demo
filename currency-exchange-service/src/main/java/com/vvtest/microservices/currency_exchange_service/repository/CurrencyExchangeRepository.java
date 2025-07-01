package com.vvtest.microservices.currency_exchange_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vvtest.microservices.currency_exchange_service.bean.CurrencyExchange;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long>{

	CurrencyExchange findByFromAndTo(String from, String to);
}
