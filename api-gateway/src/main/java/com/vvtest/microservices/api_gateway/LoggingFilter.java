package com.vvtest.microservices.api_gateway;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class LoggingFilter implements GlobalFilter{

	
	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		logger.info("Recvd request Path: {}", exchange.getRequest().getPath());
		
		//return chain.filter(exchange);
	//}
		 ServerHttpResponse originalResponse = exchange.getResponse();
	        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

    // Create a decorated response to intercept and modify the body
    ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
        @Override
        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            if (body instanceof Flux) {
                Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                return super.writeWith(
                    fluxBody.buffer().map(dataBuffers -> {
                        String originalBody = dataBuffers.stream()
                                .map(dataBuffer -> {
                                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(bytes);
                                    DataBufferUtils.release(dataBuffer);
                                    return new String(bytes, StandardCharsets.UTF_8);
                                })
                                .collect(Collectors.joining());

                        // ✅ Modify the body here
                        logger.info("originalBody"+originalBody);
                        
                        String modifiedBody = originalBody.concat("Modified.. something new");  // Example change
                        logger.info("modified"+modifiedBody);
                        byte[] newBytes = modifiedBody.getBytes(StandardCharsets.UTF_8);
                        return bufferFactory.wrap(newBytes);
                    })
                );
            }
            return super.writeWith(body);
        }
    };

    // Set the decorated response
    ServerWebExchange mutatedExchange = exchange.mutate().response(decoratedResponse).build();
    
    logger.info("Modified response");
    return chain.filter(mutatedExchange);
}
	

}
