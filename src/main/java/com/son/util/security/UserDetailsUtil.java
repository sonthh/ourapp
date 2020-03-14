package com.son.util.security;

import com.son.entity.Role;
import com.son.entity.User;
import com.son.security.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsUtil {

    public static UserDetailsImpl getCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return (UserDetailsImpl) principal;
    }

    public static UserDetailsImpl buildUserDetails(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role: user.getRoles()) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName());
            authorities.add(authority);
        }

        UserDetailsImpl credentials = new UserDetailsImpl(
            user.getId(), user.getUsername(), user.getPassword(), user.getStatus(), user.getRoles(), authorities
        );

        return credentials;
    }
}