package com.son.controller;

import com.son.dto.SalaryView;
import com.son.dto.TimeKeepingView;
import com.son.entity.Personnel;
import com.son.handler.ApiException;
import com.son.request.*;
import com.son.security.Credentials;
import com.son.service.PersonnelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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

    @ApiOperation("Add passport")
    @PutMapping("/{personnelId}/passport/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> addPassport(
            @Valid @RequestBody AddPassportRequest addPassportRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addPassport(addPassportRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update passport")
    @PutMapping("/{personnelId}/passport/update")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updatePassport(
            @Valid @RequestBody UpdatePassportRequest updatePassportRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updatePassport(updatePassportRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Add working time")
    @PutMapping("/{personnelId}/workingTime/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> addWorkingTime(
            @Valid @RequestBody AddWorkingTimeRequest addWorkingTimeRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addWorkingTime(addWorkingTimeRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update working time")
    @PutMapping("/{personnelId}/workingTime/update")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateWorkingTime(
            @Valid @RequestBody UpdateWorkingTimeRequest updateWorkingTimeRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateWorkingTime(updateWorkingTimeRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Add qualification")
    @PostMapping("/{personnelId}/qualifications")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> addQualification(
            @Valid @RequestBody AddQualificationRequest addQualificationRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addQualification(addQualificationRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update qualification")
    @PutMapping("/{personnelId}/qualifications/{qualificationId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> addQualification(
            @Valid @RequestBody UpdateQualificationRequest updateQualificationRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @Min(1) @PathVariable(value = "qualificationId", required = false) Integer qualificationId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateQualification(
                        updateQualificationRequest, personnelId, qualificationId, credentials
                ),
                HttpStatus.OK
        );
    }

    @ApiOperation("Delete qualification")
    @DeleteMapping("/{personnelId}/qualifications/{qualificationId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> deleteQualification(
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @Min(1) @PathVariable(value = "qualificationId", required = false) Integer qualificationId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.deleteQualification(personnelId, qualificationId, credentials),
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


    /*====================================WORK HISTORY START==========================================================*/
    @ApiOperation("create work history")
    @PostMapping("/{personnelId}/workHistories")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_CREATE)")
    public ResponseEntity<Personnel> createWorkHistory(
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer id,
            @Valid @RequestBody AddWorkHistoryRequest historyRequests) throws ApiException {
        return new ResponseEntity<>(personnelService.createWorkHistory(historyRequests, id), HttpStatus.OK);
    }

    @ApiOperation("update work history")
    @PutMapping("/{personnelId}/workHistories/{workHistoryId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateWorkHistory(
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer id,
            @Min(1) @PathVariable(value = "workHistoryId", required = false) Integer workHistoryId,
            @Valid @RequestBody UpdateWorkHistoryRequest historyRequests) throws ApiException {
        return new ResponseEntity<>(personnelService.updateWorkHistory(historyRequests, id, workHistoryId), HttpStatus.OK);
    }

    @ApiOperation("delete work history")
    @DeleteMapping("/{personnelId}/workHistories/{workHistoryId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_DELETE)")
    public ResponseEntity<Boolean> deleteWorkHistory(
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer id,
            @Min(1) @PathVariable(value = "workHistoryId", required = false) Integer workHistoryId) throws ApiException {
        return new ResponseEntity<>(personnelService.deleteWorkHistory(id, workHistoryId), HttpStatus.OK);
    }
    /*====================================WORK HISTORY END============================================================*/


    /*====================================CERTIFICATION START=========================================================*/
    @ApiOperation("create certification")
    @PostMapping("/{personnelId}/certifications")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_CREATE)")
    public ResponseEntity<Personnel> createCertification(
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer id,
            @Valid @RequestBody AddCertificationRequest certificationRequest) throws ApiException {
        return new ResponseEntity<>(personnelService.createCertification(certificationRequest, id), HttpStatus.OK);
    }

    @ApiOperation("update certification")
    @PutMapping("/{personnelId}/certifications/{certificationId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateCertification(
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer id,
            @Min(1) @PathVariable(value = "certificationId", required = false) Integer certificationId,
            @Valid @RequestBody UpdateCertificationRequest certificationRequest
    ) throws ApiException {
        return new ResponseEntity<>(
                personnelService.updateCertification(certificationRequest, id, certificationId), HttpStatus.OK);
    }

    @ApiOperation("delete certification")
    @DeleteMapping("/{personnelId}/certifications/{certificationId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_DELETE)")
    public ResponseEntity<Boolean> deleteCertification(
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer id,
            @Min(1) @PathVariable(value = "certificationId", required = false) Integer certificationId
    ) throws ApiException {
        return new ResponseEntity<>(personnelService.deleteCertification(id, certificationId), HttpStatus.OK);
    }
    /*====================================CERTIFICATION END===========================================================*/

    /*====================================ADDITIONAL INFO START===========================================================*/
    @ApiOperation("Add additional info")
    @PutMapping("/{personnelId}/additionalInfo/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> addAdditionalInfo(
            @Valid @RequestBody AddAdditionalInfoRequest addAdditionalInfoRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addAdditionalInfo(addAdditionalInfoRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update additional info")
    @PutMapping("/{personnelId}/additionalInfo/update")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateAdditionalInfo(
            @Valid @RequestBody UpdateAdditionalInfoRequest updateAdditionalInfoRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateAdditionalInfo(updateAdditionalInfoRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }
    /*====================================ADDITIONAL INFO END=========================================================*/

    /*====================================HEALTHY STATUS START========================================================*/

    @ApiOperation("Add healthy status")
    @PutMapping("/{personnelId}/healthyStatus/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_CREATE)")
    public ResponseEntity<Boolean> addHealthyStatus(
            @Valid @RequestBody AddHealthyStatusRequest addHealthyStatusRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addHealthyStatus(addHealthyStatusRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update healthy status")
    @PutMapping("/{personnelId}/healthyStatus/update")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateHealthyStatus(
            @Valid @RequestBody UpdateHealthyStatusRequest updateHealthyStatusRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateHealthyStatus(updateHealthyStatusRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    /*====================================HEALTHY STATUS END==========================================================*/

    /*====================================CONTACT INFO START==========================================================*/
    @ApiOperation("Add contact info")
    @PutMapping("/{personnelId}/contactInfo/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_CREATE)")
    public ResponseEntity<Boolean> addContactInfo(
            @Valid @RequestBody AddContactInfoRequest addContactInfoRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addContactInfo(addContactInfoRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update contact info")
    @PutMapping("/{personnelId}/contactInfo/update")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateContactInfo(
            @Valid @RequestBody UpdateContactInfoRequest contactInfoRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateContactInfo(contactInfoRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }
    /*====================================CONTACT INFO END============================================================*/

    /*====================================BANK INFO END===============================================================*/
    @ApiOperation("Add bank info")
    @PutMapping("/{personnelId}/bankInfo/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_CREATE)")
    public ResponseEntity<Boolean> addBankInfo(
            @Valid @RequestBody AddBankInfoRequest addBankInfoRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addBankInfo(addBankInfoRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update bank info")
    @PutMapping("/{personnelId}/bankInfo/update")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateBankInfo(
            @Valid @RequestBody UpdateBankInfoRequest updateBankInfoRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateBankInfo(updateBankInfoRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    /*====================================BANK INFO END===============================================================*/
    @ApiOperation("update avatar")
    @PostMapping("/{personnelId}/avatar")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<String> updateAvatar(
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Valid UpdateAvatarRequest updateAvatarRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId
    ) throws ApiException {
        String avatar = personnelService.updateAvatar(personnelId, updateAvatarRequest, credentials);

        return new ResponseEntity<>(avatar, HttpStatus.OK);
    }

    /*====================================SALARY END==================================================================*/
    @ApiOperation("Add salary")
    @PutMapping("/{personnelId}/salary/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_CREATE)")
    public ResponseEntity<Boolean> addSalary(
            @Valid @RequestBody AddSalaryRequest addSalaryRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addSalary(addSalaryRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update salary")
    @PutMapping("/{personnelId}/salary/update")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateSalary(
            @Valid @RequestBody UpdateSalaryRequest updateSalaryRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateSalary(updateSalaryRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }
    /*====================================SALARY END==================================================================*/

    /*====================================ALLOWANCES END==============================================================*/
    @ApiOperation("Add allowances")
    @PostMapping("/{personnelId}/allowances")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> addAllowances(
            @Valid @RequestBody AddAllowancesRequest addAllowancesRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.addAllowances(addAllowancesRequest, personnelId, credentials),
                HttpStatus.OK
        );
    }

    @ApiOperation("Update allowances")
    @PutMapping("/{personnelId}/allowances/{allowanceId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> updateAllowances(
            @Valid @RequestBody UpdateAllowancesRequest updateAllowancesRequest,
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @Min(1) @PathVariable(value = "allowanceId", required = false) Integer allowanceId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.updateAllowances(
                        updateAllowancesRequest, personnelId, allowanceId, credentials
                ),
                HttpStatus.OK
        );
    }

    @ApiOperation("Delete allowances")
    @DeleteMapping("/{personnelId}/allowances/{allowancesId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_UPDATE)")
    public ResponseEntity<Boolean> deleteAllowances(
            @Min(1) @PathVariable(value = "personnelId", required = false) Integer personnelId,
            @Min(1) @PathVariable(value = "allowancesId", required = false) Integer allowancesId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.deleteAllowances(personnelId, allowancesId, credentials),
                HttpStatus.OK
        );
    }

    /*====================================ALLOWANCES END==============================================================*/

    @ApiOperation("export personnel to excel file")
    @GetMapping("export/excel")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_READ)")
    public ResponseEntity<Resource> exportPersonnelToExcel(
            @Valid FindAllPersonnelExcelRequest request,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        String filename = "DANHSACHNHANVIEN.xlsx";

        ByteArrayInputStream is = personnelService.exportPersonnelToExcel(credentials, request);
        InputStreamResource file = new InputStreamResource(is);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @ApiOperation("calculate salary")
    @GetMapping("salary")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_PERSONNEL_READ)")
    public ResponseEntity<List<SalaryView>> findTimeKeeping(
            @Valid FindSalaryRequest findSalaryRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        return new ResponseEntity<>(
                personnelService.calculateSalary(credentials, findSalaryRequest),
                HttpStatus.OK
        );
    }

}

