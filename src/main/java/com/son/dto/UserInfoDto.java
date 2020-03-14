package com.son.dto;

import com.son.entity.Role;
import com.son.entity.UserStatus;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoDto {
    private Integer id;
    private String username;
    private UserStatus status;
    private List<Role> roles;
}
