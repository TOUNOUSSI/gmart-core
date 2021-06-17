package com.gmart.api.core.listener;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebSocketEventListener {

	private List<String> connectedClientId = new ArrayList<>();

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		log.info("Received a new web socket connection");

	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent disconnectEvent) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        log.debug("sessionId is " + disconnectEvent.getSessionId());

		List<String> nativeHeaders = headerAccessor.getNativeHeader("userId");
		Principal principal =  headerAccessor.getUser();

		if (principal != null && nativeHeaders !=null) {
			String userId = nativeHeaders.get(0);

			log.debug("Websocket disconnected. UserID disconnected : " + userId);

		}
	}

}
