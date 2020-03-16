package com.son.security;

import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.service.UserService;
import com.son.util.security.UserDetailsUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userService.findOneByUsername(username);
        } catch (ApiException e) {
            throw new UsernameNotFoundException("UsernameNotFound");
        }

        Credentials credentials = UserDetailsUtil.buildUserDetails(user);

        return credentials;
    }


}