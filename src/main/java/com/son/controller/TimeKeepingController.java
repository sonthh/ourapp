package com.son.controller;

import com.son.entity.TimeKeeping;
import com.son.handler.ApiException;
import com.son.request.DoTimeKeepingRequest;
import com.son.request.FindAllPersonnelRequest;
import com.son.request.FindAllTimeKeepingRequest;
import com.son.security.Credentials;
import com.son.service.TimeKeepingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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

@Api(tags = "Time Keeping", value = "Time Keeping Controller")
@RestController
@RequestMapping("timeKeeping")
@Validated
@RequiredArgsConstructor
public class TimeKeepingController {
    private final TimeKeepingService timeKeepingService;

    @ApiOperation("do time keeping")
    @PostMapping("{personnelId}/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIME_KEEPING_CREATE)")
    public ResponseEntity<TimeKeeping> createRequests(
            @Valid @RequestBody DoTimeKeepingRequest timeKeepingRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Min(1) @PathVariable Integer personnelId
    ) throws ApiException {
        return new ResponseEntity<>(
                timeKeepingService.doTimeKeeping(credentials, personnelId, timeKeepingRequest),
                HttpStatus.OK
        );
    }

    @ApiOperation("do time keeping")
    @GetMapping
//    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIME_KEEPING_CREATE)")
    public Object createRequests(
            @Valid FindAllTimeKeepingRequest timeKeepingRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return timeKeepingService.findTimeKeeping(credentials, timeKeepingRequest);
    }
}
