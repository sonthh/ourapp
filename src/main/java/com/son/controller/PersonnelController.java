package com.son.controller;

import com.son.entity.Personnel;
import com.son.handler.ApiException;
import com.son.request.UpdatePersonnelRequest;
import com.son.request.UpdateUserMeRequest;
import com.son.service.PersonnelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Api(tags = "Personnel", value = "Personnel Controller")
@RestController
@RequestMapping("personnel")
@Validated
@RequiredArgsConstructor
public class PersonnelController {
    @Autowired
    private PersonnelService personnelService;

    @ApiOperation("Update one personnel")
    @PutMapping("/{personnelId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_UPDATE)")
    public ResponseEntity<Personnel> updateOne(
        @Valid @RequestBody UpdatePersonnelRequest updatePersonnelRequest,
        @Min(1) @PathVariable(value = "personnelId", required = false) Integer id) throws ApiException {

        return new ResponseEntity<>(personnelService.updateOne(updatePersonnelRequest, id), HttpStatus.OK);
    }

    @ApiOperation("Delete one personnel")
    @DeleteMapping("/{personnelId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_DELETE)")
    public ResponseEntity<Boolean> deleteOne(
        @Min(1) @PathVariable(value = "personnelId", required = false) Integer id) throws ApiException {

        Boolean isDeleted = personnelService.isDeletedOne(id);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }
}
