package com.son.util.jpa;

import com.son.entity.User;
import com.son.security.UserDetailsImpl;
import com.son.util.security.UserDetailsUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        UserDetailsImpl credentials = UserDetailsUtil.getCurrentUserDetails();

        User user = credentials.toUserEntity();

        return Optional.of(user);
    }
}