package com.son.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.son.entity.User;
import com.son.entity.UserStatus;
import com.son.handler.ApiException;
import com.son.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class SocketIOService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final SocketIOServer server;

    public SocketIOService(SocketIOServer server, JwtTokenService jwtTokenService, UserRepository userRepository) {
        this.server = server;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) throws ApiException {
        if (client == null) {
            log.error("Client NULL");
            return;
        }
        System.out.println(client.getSessionId());
        String token = client.getHandshakeData().getSingleUrlParam("token");

        if (token == null) {
            throw new ApiException(200, "Token is required");
        }

        if (!jwtTokenService.validateToken(token)) {
            throw new ApiException(200, "Token is expired");
        }

        String username = jwtTokenService.getUsernameFromJWT(token);
        User user = userRepository.findActiveUser(username, UserStatus.ACTIVE);

        if (user == null) {
            throw new ApiException(200, "User not found or is inactive");
        }
        System.out.println(Arrays.toString(client.getAllRooms().toArray()));
        client.joinRoom("user-" + user.getId());
        System.out.println(Arrays.toString(client.getAllRooms().toArray()));

    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        if (client == null) {
            log.error("客户端为空");
            return;
        }
        client.disconnect();
    }

    public void sendToRoom(String roomId, String eventType, Object data) {
        server.getRoomOperations(roomId).sendEvent(eventType, data);
    }
}
