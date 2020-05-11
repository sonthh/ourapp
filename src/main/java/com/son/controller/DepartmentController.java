package com.son.controller;

import com.son.entity.Branch;
import com.son.entity.Department;
import com.son.handler.ApiException;
import com.son.request.*;
import com.son.security.Credentials;
import com.son.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Api(tags = "Departments", value = "Department Controller")
@RestController
@RequestMapping("departments")
@Validated
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @ApiOperation("get one department")
    @GetMapping("/{departmentId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_DEPARTMENT_READ)")
    public ResponseEntity<Department> findOneDepartment(
            @Min(1) @PathVariable Integer departmentId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Department department = departmentService.findOne(credentials, departmentId);

        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @ApiOperation("find many departments")
    @GetMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_DEPARTMENT_READ)")
    public ResponseEntity<Page<Department>> findMany(
            @Valid FindAllDepartmentRequest findAllDepartmentRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Page<Department> page = departmentService.findMany(credentials, findAllDepartmentRequest);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation("create one department")
    @PostMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_DEPARTMENT_CREATE)")
    public ResponseEntity<Department> createOne(
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Valid @RequestBody CreateDepartmentRequest createDepartmentRequest
    ) throws ApiException {
        Department department = departmentService.createOne(credentials, createDepartmentRequest);
        return new ResponseEntity<>(department, HttpStatus.CREATED);
    }

    @ApiOperation("update one department")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_DEPARTMENT_UPDATE)")
    @PutMapping("/{departmentId}")
    public ResponseEntity<Department> updateOne(
            @Valid @RequestBody UpdateDepartmentRequest updateDepartmentRequest,
            @Min(1) @PathVariable Integer departmentId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Department department = departmentService.updateOneDepartment(credentials, updateDepartmentRequest,
                departmentId);

        return new ResponseEntity<>(department, HttpStatus.OK);
    }

}
