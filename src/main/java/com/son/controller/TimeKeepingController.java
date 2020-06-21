package com.son.controller;

import com.son.dto.TimeKeepingView;
import com.son.entity.TimeKeeping;
import com.son.handler.ApiException;
import com.son.request.DoTimeKeepingRequest;
import com.son.request.FindAllTimeKeepingRequest;
import com.son.request.UpdateTimeKeepingRequest;
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
import java.util.List;

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
    public ResponseEntity<TimeKeeping> doTimeKeeping(
            @Valid @RequestBody DoTimeKeepingRequest timeKeepingRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Min(1) @PathVariable Integer personnelId
    ) throws ApiException {
        return new ResponseEntity<>(
                timeKeepingService.doTimeKeeping(credentials, personnelId, timeKeepingRequest),
                HttpStatus.OK
        );
    }

    @ApiOperation("update time keeping")
    @PutMapping("{personnelId}/update/{timeKeepingId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIME_KEEPING_UPDATE)")
    public ResponseEntity<TimeKeeping> updateTimeKeeping(
            @Valid @RequestBody UpdateTimeKeepingRequest timeKeepingRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Min(1) @PathVariable Integer personnelId,
            @Min(1) @PathVariable Integer timeKeepingId
    ) throws ApiException {
        return new ResponseEntity<>(
                timeKeepingService.updateTimeKeeping(credentials, personnelId, timeKeepingId, timeKeepingRequest),
                HttpStatus.OK
        );
    }

    @ApiOperation("delete time keeping")
    @DeleteMapping("{personnelId}/delete/{timeKeepingId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIME_KEEPING_DELETE)")
    public ResponseEntity<Boolean> deleteTimeKeeping(
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Min(1) @PathVariable Integer personnelId,
            @Min(1) @PathVariable Integer timeKeepingId
    ) throws ApiException {
        return new ResponseEntity<>(
                timeKeepingService.deleteTimeKeeping(credentials, personnelId, timeKeepingId),
                HttpStatus.OK
        );
    }

    @ApiOperation("find time keeping")
    @GetMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIME_KEEPING_READ)")
    public ResponseEntity<List<TimeKeepingView>> createRequests(
            @Valid FindAllTimeKeepingRequest timeKeepingRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                timeKeepingService.findTimeKeeping(credentials, timeKeepingRequest),
                HttpStatus.OK
        );
    }
}
