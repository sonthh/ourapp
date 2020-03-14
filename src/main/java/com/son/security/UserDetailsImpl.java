package com.son.security;

import com.son.entity.Role;
import com.son.entity.User;
import com.son.entity.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private UserStatus status;
    private List<Role> roles;
    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(
        Integer id, String username, String password, UserStatus status, List<Role> roles,
        List<GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.roles = roles;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == UserStatus.ACTIVE;
    }

    public User toUserEntity() {
        User user = new User();

        user.setId(this.getId());
        user.setPassword(this.getPassword());
        user.setStatus(this.getStatus());
        user.setRoles(this.getRoles());

        return user;
    }
}