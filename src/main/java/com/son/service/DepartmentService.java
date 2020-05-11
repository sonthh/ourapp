package com.son.service;

import com.son.constant.Exceptions;
import com.son.entity.Branch;
import com.son.entity.Department;
import com.son.handler.ApiException;
import com.son.repository.DepartmentRepository;
import com.son.request.CreateDepartmentRequest;
import com.son.request.FindAllDepartmentRequest;
import com.son.request.UpdateDepartmentRequest;
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
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final BranchService branchService;
    private final ModelMapper modelMapper;

    public Department findOne(Credentials credentials, Integer departmentId) throws ApiException {
        Optional<Department> optional = departmentRepository.findById(departmentId);

        if (!optional.isPresent()) {
            throw new ApiException(404, Exceptions.DEPARTMENT_NOT_FOUND);
        }

        return optional.get();
    }

    public Page<Department> findMany(Credentials credentials,
                                     FindAllDepartmentRequest findAllDepartmentRequest)
            throws ApiException {
        String createdBy = findAllDepartmentRequest.getCreatedBy();
        String lastModifiedBy = findAllDepartmentRequest.getLastModifiedBy();
        String description = findAllDepartmentRequest.getDescription();
        String branch = findAllDepartmentRequest.getBranch();
        String name = findAllDepartmentRequest.getName();
        List<Integer> departmentIds = findAllDepartmentRequest.getIds();

        Integer currentPage = findAllDepartmentRequest.getCurrentPage();
        Integer limit = findAllDepartmentRequest.getLimit();
        String sortDirection = findAllDepartmentRequest.getSortDirection();
        String sortBy = findAllDepartmentRequest.getSortBy();


        SpecificationBuilder<Department> builder = new SpecificationBuilder<>();
        builder
                .query("branch.name", CONTAINS, branch)
                .query("description", CONTAINS, description)
                .query("name", CONTAINS, name)
                .query("id", IN, departmentIds)
                .query("createdBy.username", CONTAINS, createdBy)
                .query("lastModifiedBy.username", CONTAINS, lastModifiedBy);

        Specification<Department> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(currentPage, limit, sortDirection, sortBy);

        return departmentRepository.findAll(spec, pageable);
    }

    public Department createOne(Credentials credentials, CreateDepartmentRequest createDepartmentRequest)
            throws ApiException {
        Integer branchId = createDepartmentRequest.getBranchId();
        Branch branch = branchService.findOne(credentials, branchId);

        Optional<Department> optional = departmentRepository.findOneByName(createDepartmentRequest.getName());
        if (optional.isPresent()) {
            throw new ApiException(400, Exceptions.DEPARTMENT_EXISTED);
        }

        Department savedDepartment = modelMapper.map(createDepartmentRequest, Department.class);
        savedDepartment.setBranch(branch);

        return departmentRepository.save(savedDepartment);
    }

    public Department updateOneDepartment(
            Credentials credentials,
            UpdateDepartmentRequest updateDepartmentRequest,
            Integer departmentId
    ) throws ApiException {
        Department updatedDepartment = findOne(credentials, departmentId);

        Optional<Department> optional = departmentRepository.findOneByName(updateDepartmentRequest.getName());
        if (optional.isPresent()) {
            throw new ApiException(400, Exceptions.DEPARTMENT_EXISTED);
        }

        Branch branch = null;
        if (updateDepartmentRequest.getBranchId() != null) {
            branch = branchService.findOne(credentials, updateDepartmentRequest.getBranchId());
        }
        if (branch != null) {
            updatedDepartment.setBranch(branch);
        }

        modelMapper.map(updateDepartmentRequest, updatedDepartment);

        updatedDepartment = departmentRepository.save(updatedDepartment);

        return updatedDepartment;
    }
}
