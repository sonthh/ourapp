package com.son.service;

import com.son.entity.Role;
import com.son.handler.ApiException;
import com.son.model.Scopes;
import com.son.repository.RoleRepository;
import com.son.request.UpdateRoleScopes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findOne(Integer roleId) throws ApiException {
        Optional<Role> optional = roleRepository.findById(roleId);
        if (!optional.isPresent()) {
            throw new ApiException(404, "RoleNotFound");
        }

        return optional.get();
    }

    public List<Role> findMany() throws ApiException {
        return (List<Role>) roleRepository.findAll();
    }

    public List<String> findPermissions() throws ApiException {
        return Arrays.stream(Scopes.class.getDeclaredFields())
                .map(Field::getName).collect(Collectors.toList());
    }

    public Role updateRoleScopes(Integer roleId, UpdateRoleScopes roleScopes) throws ApiException {
        Role role = findOne(roleId);
        role.setScopes(roleScopes.getScopes());
        role = roleRepository.save(role);
        return role;
    }
}
