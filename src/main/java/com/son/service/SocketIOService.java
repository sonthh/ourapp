package com.son.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.model.Event;
import com.son.request.GroupMessageRequest;
import com.son.request.JoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketIOService {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final SocketIOServer server;

    private static final String ROOM_ID_TEMPLATE = "user-%s";

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

        client.joinRoom(getRoomId(user.getId()));
        log.info("Client {} just connect to server", client.getSessionId().toString());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        if (client == null) {
            log.error("Client NULL");
            return;
        }
        client.disconnect();
    }

    @OnEvent(value = Event.JOIN)
    public void onJoinEvent(SocketIOClient client, AckRequest request, JoinRequest data) {
        log.info("User：{} just join：{}", data.getUserId(), data.getRoomId());
        client.joinRoom(data.getRoomId());

        server.getRoomOperations(data.getRoomId()).sendEvent(Event.JOIN, data);

        System.out.println("So client: " + server.getRoomOperations(data.getRoomId()).getClients().size());

    }

    @OnEvent(value = Event.GROUP)
    public void onGroupEvent(SocketIOClient client, AckRequest request, GroupMessageRequest data) {
        Collection<SocketIOClient> clients = server.getRoomOperations(data.getRoomId()).getClients();

//        boolean inGroup = false;
//        for (SocketIOClient socketIOClient : clients) {
//            if (socketIOClient.getSessionId().compareTo(client.getSessionId()) == 0) {
//                inGroup = true;
//                break;
//            }
//        }
//        if (inGroup) {
        log.info("Room {} from user {} with message：{}", data.getRoomId(), data.getUserId(), data.getMessage());
        sendToRoom(data.getRoomId(), Event.GROUP, data);
//        }
    }

    public void sendToRoom(String roomId, String eventType, Object data) {
        server.getRoomOperations(roomId).sendEvent(eventType, data);
    }

    private String getRoomId(Integer userId) {
        return String.format(ROOM_ID_TEMPLATE, userId);
    }
}
