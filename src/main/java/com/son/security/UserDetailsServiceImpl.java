package com.son.security;

import com.son.entity.User;
import com.son.entity.UserStatus;
import com.son.repository.UserRepository;
import com.son.util.security.UserDetailsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        UserDetailsImpl userDetails = UserDetailsUtil.buildUserDetail(user);
        BeanUtils.copyProperties(user, userDetails);

        return userDetails;
    }




}