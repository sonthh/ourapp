package com.son.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.son.model.Role;
import com.son.model.Roles;
import com.son.props.AppProps;
import com.son.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final AppProps appProps;
    private final SocketIOServer server;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (appProps.getUpdateRoles()) {
            updateRoles();
        }
        server.start();
        log.info("My app has been started at " + appProps.getEnvironment());
    }

    public void updateRoles() {
        Map<String, String[]> roles = new HashMap<>();
        roles.put(Role.MANAGER, Roles.MANAGER);
        roles.put(Role.ADMIN, Roles.ADMIN);
        roles.put(Role.BASIC, Roles.BASIC);

        for (Map.Entry<String, String[]> entry : roles.entrySet()) {
            String roleName = entry.getKey();
            String[] scopes = entry.getValue();
            Optional<com.son.entity.Role> optionalRole = roleRepository.findOneByName(roleName);
            com.son.entity.Role role;

            if (optionalRole.isPresent()) {
                role = optionalRole.get();
                role.setScopes(Arrays.asList(scopes));
            } else {
                role = new com.son.entity.Role(null, roleName, Arrays.asList(scopes));
            }

            roleRepository.save(role);
        }
    }
}
