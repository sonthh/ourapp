package com.son.controller;

import com.son.dto.TimeKeepingView;
import com.son.entity.TimeKeeping;
import com.son.handler.ApiException;
import com.son.request.DoTimeKeepingRequest;
import com.son.request.FindAllTimeKeepingExcelRequest;
import com.son.request.FindAllTimeKeepingRequest;
import com.son.request.UpdateTimeKeepingRequest;
import com.son.security.Credentials;
import com.son.service.TimeKeepingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.ByteArrayInputStream;
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
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIMEKEEPING_CREATE)")
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
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIMEKEEPING_UPDATE)")
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
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIMEKEEPING_DELETE)")
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
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIMEKEEPING_READ)")
    public ResponseEntity<List<TimeKeepingView>> findTimeKeeping(
            @Valid FindAllTimeKeepingRequest timeKeepingRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                timeKeepingService.findTimeKeeping(credentials, timeKeepingRequest),
                HttpStatus.OK
        );
    }

    @ApiOperation("export time keeping (excel)")
    @GetMapping("export/excel")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_TIMEKEEPING_READ)")
    public ResponseEntity<Resource> exportTimeKeeping(
            @Valid FindAllTimeKeepingExcelRequest timeKeepingRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        String fileName = "Bảngchấmcông.xlsx";
        ByteArrayInputStream is = timeKeepingService.exportTimeKeeping(credentials, timeKeepingRequest);
        InputStreamResource file = new InputStreamResource(is);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
