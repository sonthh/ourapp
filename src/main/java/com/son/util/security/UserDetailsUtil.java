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
        List<Role> roles = user.getRoles();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getName()));

            List<String> scopes = role.getScopes();
            for (String scope : scopes) {
                authorities.add(new SimpleGrantedAuthority(scope));
            }
        }

        return new Credentials(
                user.getId(), user.getUsername(), user.getPassword(), user.getFullName(), user.getEmail(),
                user.getPhoneNumber(), user.getBirthDay(), user.getGender(), user.getIdentification(),
                user.getAddress(), user.getAvatar(), user.getStatus(), user.getShouldSendNotification(),
                user.getNotificationTypes(), user.getRoles(), authorities, user
        );
    }
}
