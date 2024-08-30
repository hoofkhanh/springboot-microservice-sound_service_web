package com.hokhanh.web.websocket;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class UserHandshakeHandler extends DefaultHandshakeHandler {

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {

		String query = request.getURI().getQuery(); // idAndRole=1321312
		String[] keyValue = query.split("=");	
		String idAndRole = keyValue[1];
		System.out.println(idAndRole);

		return new UserPrincipal() {

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return idAndRole;
			}
		};
	}

}
