package com.son.controller;

import com.son.entity.Personnel;
import com.son.handler.ApiException;
import com.son.request.*;
import com.son.security.Credentials;
import com.son.service.PersonnelService;
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

@Api(tags = "Personnel", value = "Personnel Controller")
@RestController
@RequestMapping("personnel")
@Validated
@RequiredArgsConstructor
public class PersonnelController {

    private final PersonnelService personnelService;

    @ApiOperation("Create one personnel with basic info")
    @PostMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_CREATE)")
    public ResponseEntity<Personnel> createOnePersonnel(
            @Valid @RequestBody CreatePersonnelRequest createPersonnelRequest
    ) throws ApiException {

        return new ResponseEntity<>(personnelService.createOne(createPersonnelRequest), HttpStatus.OK);
    }

    @ApiOperation("Update one personnel / basic information")
    @PutMapping("/{personnelId}/basicInfo")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Personnel> updateBasicInfo(
            @Valid @RequestBody UpdatePersonnelBasicInfo personnelRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateBasicInfo(personnelRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Add identification")
    @PutMapping("/{personnelId}/identification/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> addIdentification(
            @Valid @RequestBody AddIdentificationRequest addIdentificationRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addIdentification(addIdentificationRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update identification")
    @PutMapping("/{personnelId}/identification/update")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateIdentification(
            @Valid @RequestBody UpdateIdentificationRequest updateIdentificationRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateIdentification(updateIdentificationRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Delete one personnel")
    @DeleteMapping("/{personnelId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_DELETE)")
    public ResponseEntity<Boolean> deleteOne(
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer id) throws ApiException {

        Boolean isDeleted = personnelService.isDeletedOne(id);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    @ApiOperation("Delete many personnels")
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_DELETE)")
    public ResponseEntity<Boolean> deleteMany(@Valid @RequestBody DeleteManyByIdRequest deleteManyByIdRequest)
            throws ApiException {

        Boolean isDeleted = personnelService.deteleMany(deleteManyByIdRequest);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    @ApiOperation("get one personnel")
    @GetMapping("/{personnelId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_READ)")
    public ResponseEntity<Personnel> findOnePersonnel(
            @Min(1) @PathVariable Integer personnelId
    ) throws ApiException {
        Personnel personnel = personnelService.findOne(personnelId);

        return new ResponseEntity<>(personnel, HttpStatus.OK);
    }

    @ApiOperation("find many personnel")
    @GetMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_READ)")
    public ResponseEntity<Page<Personnel>> findMany(
            @Valid FindAllPersonnelRequest findAllPersonnelRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Page<Personnel> page = personnelService.findMany(credentials, findAllPersonnelRequest);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
