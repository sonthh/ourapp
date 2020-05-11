package com.son.service;

import com.son.constant.Exceptions;
import com.son.entity.Branch;
import com.son.entity.Role;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.repository.BranchRepository;
import com.son.request.CreateBranchRequest;
import com.son.request.FindAllBranchRequest;
import com.son.request.UpdateBranchRequest;
import com.son.request.UpdateUserRequest;
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

import static com.son.util.spec.SearchOperation.CONTAINS;
import static com.son.util.spec.SearchOperation.IN;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;

    public Branch createOne(Credentials credentials, CreateBranchRequest createBranchRequest)
            throws ApiException {
        Branch savedBranch = modelMapper.map(createBranchRequest, Branch.class);

        return branchRepository.save(savedBranch);
    }

    public Branch updateOneBranch(Credentials credentials, UpdateBranchRequest updateBranchRequest,
                              Integer branchId)
            throws ApiException {
        Branch updatedBranch = findOne(credentials, branchId);

        modelMapper.map(updateBranchRequest, updatedBranch);

        updatedBranch = branchRepository.save(updatedBranch);

        return updatedBranch;
    }

    public Branch findOne(Credentials credentials, Integer branchId) throws ApiException {
        Optional<Branch> optional = branchRepository.findById(branchId);

        if (!optional.isPresent()) {
            throw new ApiException(404, Exceptions.BRANCH_NOT_FOUND);
        }

        return optional.get();
    }

    public Page<Branch> findMany(Credentials credentials, FindAllBranchRequest findAllBranchRequest)
            throws ApiException {
        String createdBy = findAllBranchRequest.getCreatedBy();
        String lastModifiedBy = findAllBranchRequest.getLastModifiedBy();
        String description = findAllBranchRequest.getDescription();
        String location = findAllBranchRequest.getLocation();
        String name = findAllBranchRequest.getName();
        List<Integer> branchIds = findAllBranchRequest.getIds();

        Integer currentPage = findAllBranchRequest.getCurrentPage();
        Integer limit = findAllBranchRequest.getLimit();
        String sortDirection = findAllBranchRequest.getSortDirection();
        String sortBy = findAllBranchRequest.getSortBy();


        SpecificationBuilder<Branch> builder = new SpecificationBuilder<>();
        builder
                .query("location", CONTAINS, location)
                .query("description", CONTAINS, description)
                .query("name", CONTAINS, name)
                .query("id", IN, branchIds)
                .query("createdBy.username", CONTAINS, createdBy)
                .query("lastModifiedBy.username", CONTAINS, lastModifiedBy);

        Specification<Branch> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(currentPage, limit, sortDirection, sortBy);

        return branchRepository.findAll(spec, pageable);
    }
}
