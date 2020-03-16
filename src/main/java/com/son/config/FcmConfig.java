package com.son.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.son.props.FcmProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties({FcmProps.class})
@Slf4j
public class FcmConfig {

    @Bean
    public FirebaseApp initialize(FcmProps fcmProps) {
        FirebaseApp firebaseApp = null;
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials
                .fromStream(new ClassPathResource(fcmProps.getConfigFile()).getInputStream())).build();

            if (FirebaseApp.getApps().isEmpty()) {
                firebaseApp = FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            } else {
                firebaseApp = FirebaseApp.getInstance();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return firebaseApp;
    }
}
