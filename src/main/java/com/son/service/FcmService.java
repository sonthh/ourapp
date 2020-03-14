package com.son.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FcmService {
    @Value("${firebase-config}")
    private String firebaseConfigPath;

    private FirebaseApp firebaseApp;

    @PostConstruct
    public void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())).build();

            if (FirebaseApp.getApps().isEmpty()) {
                this.firebaseApp = FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
                return;
            }

            this.firebaseApp = FirebaseApp.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

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

        Message message = Message.builder()
            .setTopic(target)
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .setImage("https://i.pinimg.com/originals/62/de/73/62de73548da3585435dc7bf6050a77b3.jpg")
                    .build()
            )
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
