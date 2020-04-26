package com.son.controller;

import com.son.entity.Role;
import com.son.handler.ApiException;
import com.son.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "Roles", value = "Role Controller")
@RestController
@RequestMapping("roles")
@Validated
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @ApiOperation("find many roles")
    @GetMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_ROLE_READ)")
    public ResponseEntity<List<Role>> findMany() throws ApiException {
        List<Role> roles = roleService.findMany();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @ApiOperation("get one user")
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_ROLE_READ)")
    public ResponseEntity<Role> findOneUser(
            @Min(1) @PathVariable Integer roleId
    ) throws ApiException {
        Role role = roleService.findOne(roleId);

        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
