package com.lauchilus.chat.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.lauchilus.chat.controller.ChatMessage;
import com.lauchilus.chat.controller.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
	
	private final SimpMessageSendingOperations messageTemplate;
	
	@EventListener
	public void handlerWebSockerDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccesor.getSessionAttributes().get("username");
		if(username != null) {
			log.info("User disconnected: {}",username);
			var chatMessage = ChatMessage.builder()
					.type(MessageType.LEAVE)
					.sender(username)
					
					.build();
			messageTemplate.convertAndSend("/topic/public",chatMessage);
		}
	}
	
}
