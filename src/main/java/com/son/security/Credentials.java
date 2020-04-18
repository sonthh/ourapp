package com.son.security;

import com.son.entity.Role;
import com.son.entity.User;
import com.son.model.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Credentials implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Date birthDay;
    private Gender gender;
    private String identification;
    private String address;
    private String avatar;
    private User.Status status;
    private Boolean shouldSendNotification;
    private List<String> notificationTypes;
    private List<Role> roles;
    private List<GrantedAuthority> authorities;
    private User userEntity;

    public Credentials(
            Integer id, String username, String password, String fullName, String email, String phoneNumber,
            Date birthDate, Gender gender, String identification, String address, String avatar, User.Status status,
            Boolean shouldSendNotification, List<String> notificationTypes, List<Role> roles,
            List<GrantedAuthority> authorities, User userEntity
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDate;
        this.gender = gender;
        this.identification = identification;
        this.address = address;
        this.avatar = avatar;
        this.status = status;
        this.shouldSendNotification = shouldSendNotification;
        this.notificationTypes = notificationTypes;
        this.roles = roles;
        this.authorities = authorities;
        this.userEntity = userEntity;
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

    public Boolean hasRole(String roleName) {
        if (roles == null || roles.isEmpty() || roleName == null || roleName.trim().equals("")) {
            return false;
        }

        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }

    public Boolean isAdmin() {
        return hasRole(com.son.model.Role.ADMIN);
    }

    public Boolean isManager() {
        return hasRole(com.son.model.Role.MANAGER);
    }

    public boolean sameEntity(User user) {
        return user.getId().equals(this.id);
    }
}
