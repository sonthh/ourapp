package com.son.controller;

import com.son.entity.Requests;
import com.son.handler.ApiException;
import com.son.request.AddRequests;
import com.son.request.UpdateRequests;
import com.son.security.Credentials;
import com.son.service.RequestsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Api(tags = "Requests", value = "Requests Controller")
@RestController
@RequestMapping("requests")
@Validated
@RequiredArgsConstructor
public class RequestsController {
    private final RequestsService requestsService;

    /*====================================REQUESTS START==============================================================*/
    @ApiOperation("create requests")
    @PostMapping()
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_CREATE)")
    public ResponseEntity<Requests> createRequests(@Valid @RequestBody AddRequests addRequests,
                                                   @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        return new ResponseEntity<>(requestsService.createRequests(addRequests, credentials), HttpStatus.OK);
    }

    @ApiOperation("delete requests")
    @DeleteMapping("/{requestsId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_DELETE)")
    public ResponseEntity<Boolean> deleteRequests(
        @Min(1) @PathVariable(value = "requestsId", required = false) Integer requestsId,
        @ApiIgnore @AuthenticationPrincipal Credentials credentials) throws ApiException {
        return new ResponseEntity<>(requestsService.deleteRequests(requestsId, credentials), HttpStatus.OK);
    }

    @ApiOperation("update requests")
    @PutMapping("/{requestsId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Requests> updateRequests(
        @Min(1) @PathVariable(value = "requestsId", required = false) Integer requestsId,
        @Valid @RequestBody UpdateRequests updateRequests,
        @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        return new ResponseEntity<>(requestsService.updateRequests(requestsId, updateRequests, credentials), HttpStatus.OK);
    }
    /*====================================REQUESTS END================================================================*/
}