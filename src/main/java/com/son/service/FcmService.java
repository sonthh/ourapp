package com.son.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.son.props.FcmProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@EnableConfigurationProperties({FcmProps.class})
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseApp firebaseApp;

    public void subscribeToTopic(List<String> tokens, String topic) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(tokens, topic);
        } catch (FirebaseMessagingException e) {
            log.error("Firebase subscribe to topic fail", e);
        }
    }

    public void unsubscribeFromTopic(List<String> tokens, String topic) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).unsubscribeFromTopic(tokens, topic);
        } catch (FirebaseMessagingException e) {
            log.error("Firebase unsubscribe from topic fail", e);
        }
    }

    public String sendToTopic(String target, String title, String body) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        data.put("body", body);
        data.put("title", title);
        ObjectMapper mapper = new JsonMapper();
        String json = mapper.writeValueAsString(data);

        Notification notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .setImage("https://i.pinimg.com/originals/62/de/73/62de73548da3585435dc7bf6050a77b3.jpg")
            .build();

        Message message = Message.builder()
            .setTopic(target)
            .setNotification(notification)
            .putData("body", json)
            .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
        }

        return response;
    }
}
