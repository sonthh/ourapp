package com.son.security;

import com.son.entity.Role;
import com.son.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class Credentials implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private User.Status status;
    private List<Role> roles;
    private List<GrantedAuthority> authorities;

    public Credentials(
        Integer id, String username, String password, User.Status status, List<Role> roles,
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
        return this.status == User.Status.ACTIVE;
    }

    public User toUserEntity() {
        User user = new User();

        user.setId(this.getId());
        user.setPassword(this.getPassword());
        user.setStatus(this.getStatus());
        user.setRoles(this.getRoles());

        return user;
    }

    public Boolean hasRole(String roleName) {
        if (roles == null || roles.isEmpty() || roleName == null || roleName.trim().equals("")) {
            return false;
        }

        Boolean hasRole = roles.stream().anyMatch(role -> role.getName().equals(roleName));

        return hasRole;
    }

    public Boolean isAdmin() {
        return hasRole(com.son.model.Role.ADMIN);
    }
}
