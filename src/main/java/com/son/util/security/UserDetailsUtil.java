package com.son.util.security;

import com.son.entity.Role;
import com.son.entity.User;
import com.son.security.Credentials;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsUtil {

    public static final String ROLE_PREFIX = "ROLE_";

    public static Credentials getCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return (Credentials) principal;
    }

    public static Credentials buildUserDetails(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role: user.getRoles()) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + role.getName());
            authorities.add(authority);
        }

        Credentials credentials = new Credentials(
            user.getId(), user.getUsername(), user.getPassword(), user.getStatus(), user.getRoles(),
            user.getEmail(), user.getAvatar(), authorities
        );

        return credentials;
    }
}
