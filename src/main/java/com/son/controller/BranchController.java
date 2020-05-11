package com.son.controller;

import com.son.entity.Branch;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.request.CreateBranchRequest;
import com.son.request.FindAllBranchRequest;
import com.son.request.UpdateBranchRequest;
import com.son.request.UpdateUserRequest;
import com.son.security.Credentials;
import com.son.service.BranchService;
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

@Api(tags = "Branches", value = "Branch Controller")
@RestController
@RequestMapping("branches")
@Validated
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @ApiOperation("get one branch")
    @GetMapping("/{branchId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_BRANCH_READ)")
    public ResponseEntity<Branch> findOneBranch(
            @Min(1) @PathVariable Integer branchId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Branch branch = branchService.findOne(credentials, branchId);

        return new ResponseEntity<>(branch, HttpStatus.OK);
    }

    @ApiOperation("find many branches")
    @GetMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_BRANCH_READ)")
    public ResponseEntity<Page<Branch>> findMany(
            @Valid FindAllBranchRequest findAllBranchRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Page<Branch> page = branchService.findMany(credentials, findAllBranchRequest);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation("create one branch")
    @PostMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_BRANCH_CREATE)")
    public ResponseEntity<Branch> createOne(
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Valid @RequestBody CreateBranchRequest createBranchRequest
    ) throws ApiException {
        Branch branch = branchService.createOne(credentials, createBranchRequest);
        return new ResponseEntity<>(branch, HttpStatus.CREATED);
    }

    @ApiOperation("update one branch")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_BRANCH_UPDATE)")
    @PutMapping("/{branchId}")
    public ResponseEntity<Branch> updateOne(
            @Valid @RequestBody UpdateBranchRequest updateBranchRequest,
            @Min(1) @PathVariable Integer branchId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Branch updatedUser = branchService.updateOneBranch(credentials, updateBranchRequest, branchId);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
