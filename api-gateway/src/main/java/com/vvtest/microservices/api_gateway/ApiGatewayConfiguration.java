package com.vvtest.microservices.api_gateway;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		
		Function<PredicateSpec, Buildable<Route>> routeFunction =
				
				p-> p.path("/get")
					.filters(f -> f.addRequestHeader("MyHeader", "MYRIEWRE"))
					.uri("http://httpbin.org:80");
				
				
				return builder.routes()
						.route(routeFunction)
						.route(a -> a.path("/currency-exchange/**").uri("lb://currency-exchange"))
						.route(a -> a.path("/currency-conversion/**").uri("lb://currency-conversion"))
						.route(a -> a.path("/currency-conversion-feign/**").uri("lb://currency-conversion"))
						.route(a -> a.path("/cur-conv/**")
								.filters(f-> f.rewritePath("/cur-conv/(?<segment>.*)",
										"/currency-conversion-feign/${segment}"))
								.uri("lb://currency-conversion"))
						.build();
								
					
	}
	
}
