package com.son.controller;

import com.son.entity.Contract;
import com.son.handler.ApiException;
import com.son.request.AddContractRequest;
import com.son.request.FindAllContractRequest;
import com.son.request.UpdateContractRequest;
import com.son.security.Credentials;
import com.son.service.ContractService;
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

@Api(tags = "Contract", value = "Contract Controller")
@RestController
@RequestMapping("contracts")
@Validated
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;

    /*====================================CONTRACT START==============================================================*/
    @ApiOperation("create contract")
    @PostMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_CONTRACT_CREATE)")
    public ResponseEntity<Contract> createOne(
            @Valid @RequestBody AddContractRequest addContractRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        return new ResponseEntity<>(contractService.createOne(addContractRequest, credentials), HttpStatus.OK);
    }

    @ApiOperation("delete contract")
    @DeleteMapping("/{contractId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_CONTRACT_DELETE)")
    public ResponseEntity<Boolean> deleteOne(
            @Min(1) @PathVariable(value = "contractId") Integer contractId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        return new ResponseEntity<>(contractService.deleteOne(contractId, credentials), HttpStatus.OK);
    }

    @ApiOperation("update contract")
    @PutMapping("/{contractId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_CONTRACT_UPDATE)")
    public ResponseEntity<Contract> updateOne(
            @Min(1) @PathVariable(value = "contractId") Integer contractId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Valid @RequestBody UpdateContractRequest updateContractRequest
    ) throws ApiException {
        return new ResponseEntity<>(
                contractService.updateOne(contractId, credentials, updateContractRequest), HttpStatus.OK);
    }

    @ApiOperation("find one contract")
    @GetMapping("/{contractId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_CONTRACT_READ)")
    public ResponseEntity<Contract> findOne(
            @Min(1) @PathVariable(value = "contractId") Integer contractId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        return new ResponseEntity<>(
                contractService.findOne(contractId),
                HttpStatus.OK
        );
    }

    @ApiOperation("find many contract")
    @GetMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_CONTRACT_READ)")
    public ResponseEntity<Page<Contract>> findMany(
            @Valid FindAllContractRequest findAllContractRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Page<Contract> page = contractService.findMany(credentials, findAllContractRequest);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /*====================================CONTRACT END================================================================*/
}
