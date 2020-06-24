package com.son.controller;

import com.son.entity.Requests;
import com.son.handler.ApiException;
import com.son.request.AddRequest;
import com.son.request.FindAllRequests;
import com.son.request.UpdateRequest;
import com.son.security.Credentials;
import com.son.service.RequestsService;
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

@Api(tags = "Requests", value = "Request Controller")
@RestController
@RequestMapping("requests")
@Validated
@RequiredArgsConstructor
public class RequestsController {
    private final RequestsService requestsService;

    @ApiOperation("create one request")
    @PostMapping()
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_CREATE)")
    public ResponseEntity<Requests> createOneRequest(
            @Valid @RequestBody AddRequest addRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        return new ResponseEntity<>(requestsService.createOneRequest(addRequest, credentials), HttpStatus.OK);
    }

    @ApiOperation("delete one request")
    @DeleteMapping("/{requestId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_DELETE)")
    public ResponseEntity<Boolean> deleteOneRequest(
            @Min(1) @PathVariable(value = "requestId", required = false) Integer requestsId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        return new ResponseEntity<>(requestsService.deleteOneRequest(requestsId, credentials), HttpStatus.OK);
    }

    @ApiOperation("update one request")
    @PutMapping("/{requestId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Requests> updateOneRequest(
            @Min(1) @PathVariable(value = "requestId", required = false) Integer requestId,
            @Valid @RequestBody UpdateRequest updateRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        return new ResponseEntity<>(
                requestsService.updateOneRequest(requestId, updateRequest, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("find one request")
    @GetMapping("/{requestId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Requests> findOneRequest(
            @Min(1) @PathVariable(value = "requestId", required = false) Integer requestId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        return new ResponseEntity<>(
                requestsService.findOne(requestId),
                HttpStatus.OK
        );
    }

    @ApiOperation("find many requests")
    @GetMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_READ)")
    public ResponseEntity<Page<Requests>> findMany(
            @Valid FindAllRequests findAllRequests,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Page<Requests> page = requestsService.findMany(credentials, findAllRequests);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
