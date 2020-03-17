package com.son.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.son.entity.User;
import com.son.handler.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SocketIOService {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final SocketIOServer server;

    public SocketIOService(SocketIOServer server, JwtTokenService jwtTokenService, UserService userService) {
        this.server = server;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) throws ApiException {
        if (client == null) {
            log.error("Client NULL");
            return;
        }

        String token = client.getHandshakeData().getSingleUrlParam("token");

        if (token == null) {
            throw new ApiException(200, "Token is required");
        }

        if (!jwtTokenService.validateToken(token)) {
            throw new ApiException(200, "Token is expired");
        }

        String username = jwtTokenService.getUsernameFromJWT(token);

        User user = userService.findOneActiveUser(username);

        client.joinRoom("user-" + user.getId());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        if (client == null) {
            log.error("Client NULL");
            return;
        }
        client.disconnect();
    }

    public void sendToRoom(String roomId, String eventType, Object data) {
        server.getRoomOperations(roomId).sendEvent(eventType, data);
    }
}
