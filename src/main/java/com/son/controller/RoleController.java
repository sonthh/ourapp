package com.son.controller;

import com.son.entity.Role;
import com.son.handler.ApiException;
import com.son.request.UpdateRoleScopes;
import com.son.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @ApiOperation("get all permission")
    @GetMapping("/permissions")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERMISSION_READ)")
    public ResponseEntity<List<String>> findManyPermission() throws ApiException {
        List<String> permissions = roleService.findPermissions();

        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

    @ApiOperation("update scopes")
    @PutMapping("/{roleId}/scopes")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_ROLE_UPDATE)")
    public ResponseEntity<Role> updateScopes(
            @Min(1) @PathVariable Integer roleId,
            @Valid @RequestBody UpdateRoleScopes roleScopes
    ) throws ApiException {
        Role role = roleService.updateRoleScopes(roleId, roleScopes);

        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
