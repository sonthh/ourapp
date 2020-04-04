package com.son.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.son.props.AppProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final AppProps appProps;
    private final SocketIOServer server;

    @Override
    public void run(String... args) {
        // server.start();
        log.info("My app has been started at " + appProps.getEnvironment());
    }
}
