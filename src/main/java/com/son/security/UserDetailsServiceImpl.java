package com.son.security;

import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.service.UserService;
import com.son.util.security.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userService.findOneByUsername(username);
        } catch (ApiException e) {
            throw new UsernameNotFoundException("UsernameNotFound");
        }

        return UserDetailsUtil.buildUserDetails(user);
    }
}
