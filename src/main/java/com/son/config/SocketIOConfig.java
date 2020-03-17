package com.son.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.son.props.WsProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({WsProps.class})
public class SocketIOConfig {

    public static final String PARAMETER_TOKEN = "token";

    @Bean
    public SocketIOServer server(WsProps wsProps) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

        config.setHostname(wsProps.getHost());
        config.setPort(wsProps.getPort());

        SocketConfig sockConfig = new SocketConfig();
        sockConfig.setReuseAddress(true);

        config.setSocketConfig(sockConfig);

        config.setAuthorizationListener(data -> {
            String token = data.getSingleUrlParam(PARAMETER_TOKEN);
            return token != null && !token.trim().equals("");
        });

        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer server) {
        return new SpringAnnotationScanner(server);
    }
}
