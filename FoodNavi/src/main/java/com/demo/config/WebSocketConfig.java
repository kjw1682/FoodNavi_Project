package com.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import lombok.RequiredArgsConstructor;

// handler 를 이용해서 websocket을 활성화하기위한 config파일
// @EnableWebSocket 을 작성하여 활성화
// endpoint 작성 /ws/chat
// CORS : setAllowedOrigins("*") = 모든 ip에서 접속이 가능하도록 하는 부분

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config){
		config.enableSimpleBroker("/sub");
		config.setApplicationDestinationPrefixes("/pub");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint( "/ws-stomp").setAllowedOriginPatterns("*")
				.withSockJS();

	}

}
