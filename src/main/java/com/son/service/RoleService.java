package com.son.service;

import com.son.entity.Role;
import com.son.handler.ApiException;
import com.son.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
