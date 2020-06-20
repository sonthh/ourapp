package com.son.service;

import com.son.constant.Exceptions;
import com.son.entity.Contract;
import com.son.entity.Personnel;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.repository.ContractRepository;
import com.son.request.AddContractRequest;
import com.son.request.FindAllContractRequest;
import com.son.request.UpdateContractRequest;
import com.son.security.Credentials;
import com.son.util.page.PageUtil;
import com.son.util.spec.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.son.util.spec.SearchOperation.*;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;

    private final PersonnelService personnelService;
    private final UserService userService;

    private final ModelMapper modelMapper;

    /*====================================CONTRACT START==============================================================*/
    public Contract findOne(Integer contractId) throws ApiException {
        Optional<Contract> contract = contractRepository.findById(contractId);
        if (!contract.isPresent()) {
            throw new ApiException(400, Exceptions.CONTRACT_NOT_FOUND);
        }
        return contract.get();
    }

    public Contract findOneByContractNumber(String contractNumber) throws ApiException {
        Optional<Contract> contract = contractRepository.findByContractNumber(contractNumber);
        if (!contract.isPresent()) {
            throw new ApiException(400, Exceptions.CONTRACT_NOT_FOUND);
        }
        return contract.get();
    }

    public Contract createOne(AddContractRequest addContractRequest, Credentials credentials) throws ApiException {
        Personnel personnel = personnelService.findOne(addContractRequest.getPersonnelId());
        // User signer = userService.findOne(addContractRequest.getSignerId());
        String contractNumber = addContractRequest.getContractNumber();

        Contract contract = modelMapper.map(addContractRequest, Contract.class);
        if (contractNumber != null) {
            Optional<Contract> optional = contractRepository.findByContractNumber(contractNumber);
            if (optional.isPresent()) {
                throw new ApiException(400, Exceptions.CONTRACT_EXISTED);
            } else {
                contract.setContractNumber(contractNumber);
            }
        }
        contract.setPersonnel(personnel);
        // contract.setSigner(signer);

        return contractRepository.save(contract);
    }

    public Boolean deleteOne(Integer contractId, Credentials credentials) throws ApiException {
        Contract contract = findOne(contractId);

        if (credentials.getUserEntity().getUsername().equals("admin") ||
                credentials.getUserEntity().getId().equals(contract.getSigner().getId())) {
            contractRepository.delete(contract);
            return true;
        }
        throw new ApiException(400, Exceptions.CONTRACT_NOT_FOUND);
    }

    public Contract updateOne(Integer contractId, Credentials credentials, UpdateContractRequest updateContractRequest)
            throws ApiException {
        Contract contract = findOne(contractId);

        String contractNumber = updateContractRequest.getContractNumber();
        Integer userId = updateContractRequest.getSignerId();
        Integer personnelId = updateContractRequest.getPersonnelId();

        if (contractNumber != null) {
            Optional<Contract> optional = contractRepository.findByContractNumber(contractNumber);
            if (optional.isPresent() && !optional.get().getId().equals(contract.getId())) {
                throw new ApiException(400, Exceptions.CONTRACT_EXISTED);
            } else {
                contract.setContractNumber(contractNumber);
            }
        }
        if (userId != null) {
            User signer = userService.findOne(userId);
            contract.setSigner(signer);
        }
        if (personnelId != null) {
            Personnel personnel = personnelService.findOne(personnelId);
            contract.setPersonnel(personnel);
        }

        modelMapper.map(updateContractRequest, contract);
        return contractRepository.save(contract);
    }

    public Page<Contract> findMany(Credentials credentials, FindAllContractRequest findAll)
            throws ApiException {
        String createdBy = findAll.getCreatedBy();
        String lastModifiedBy = findAll.getLastModifiedBy();
        String fullName = findAll.getFullName();
        String contractType = findAll.getContractType();
        Integer personnelId = findAll.getPersonnelId();
        List<Integer> ids = findAll.getIds();

        Integer currentPage = findAll.getCurrentPage();
        Integer limit = findAll.getLimit();
        String sortDirection = findAll.getSortDirection();
        String sortBy = findAll.getSortBy();


        SpecificationBuilder<Contract> builder = new SpecificationBuilder<>();
        builder
                .query("personnel.fullName", CONTAINS, fullName)
                .query("id", IN, ids)
                .query("personnel.id", EQUALITY, personnelId)
                .query("contractType", CONTAINS, contractType)
                .query("createdBy.username", CONTAINS, createdBy)
                .query("lastModifiedBy.username", CONTAINS, lastModifiedBy);

        Specification<Contract> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(currentPage, limit, sortDirection, sortBy);

        return contractRepository.findAll(spec, pageable);
    }
    /*====================================CONTRACT END================================================================*/
}
