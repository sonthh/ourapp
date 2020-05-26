package com.son.service;

import com.son.constant.Exceptions;
import com.son.entity.*;
import com.son.handler.ApiException;
import com.son.model.Gender;
import com.son.repository.*;
import com.son.request.*;
import com.son.security.Credentials;
import com.son.util.common.EnumUtil;
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
public class PersonnelService {
    private final PersonnelRepository personnelRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;
    private final UserRepository userRepository;
    private final IdentificationRepository identificationRepository;
    private final PassportRepository passportRepository;
    private final WorkingTimeRepository workingTimeRepository;
    private final ModelMapper modelMapper;

    public Personnel findOne(Integer personnelId) throws ApiException {
        Optional<Personnel> optional = personnelRepository.findById(personnelId);
        if (!optional.isPresent()) {
            throw new ApiException(404, Exceptions.PERSONNEL_NOT_FOUND);
        }

        return optional.get();
    }

    public Boolean isDeletedOne(int personnelId) throws ApiException {
        Optional<Personnel> personnel = personnelRepository.findById(personnelId);
        if (!personnel.isPresent()) {
            throw new ApiException(404, Exceptions.PERSONNEL_NOT_FOUND);
        }
        personnelRepository.deleteById(personnelId);
        return true;
    }

    public Boolean deteleMany(DeleteManyByIdRequest deleteManyByIdRequest) throws ApiException {
        List<Integer> ids = deleteManyByIdRequest.getIds();
        List<Personnel> personnels = (List<Personnel>) personnelRepository.findAllById(ids);

        if (personnels.size() != ids.size()) {
            throw new ApiException(404, Exceptions.PERSONNEL_NOT_FOUND);
        }

        personnelRepository.deleteAll(personnels);

        return true;
    }

    // create one personnel with basic information
    public Personnel createOne(CreatePersonnelRequest createPersonnelRequest) throws ApiException {
        Optional<Department> department = departmentRepository.findById(createPersonnelRequest.getDepartmentId());

        if (!department.isPresent()) {
            throw new ApiException(404, Exceptions.DEPARTMENT_NOT_FOUND);
        }

        Personnel newPersonnel = modelMapper.map(createPersonnelRequest, Personnel.class);

        newPersonnel.setDepartment(department.get());

        return personnelRepository.save(newPersonnel);
    }

    public Personnel updateBasicInfo(
            UpdatePersonnelBasicInfo personnelRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Integer departmentId = personnelRequest.getDepartmentId();
        if (departmentId != null) {
            Department department = departmentService.findOne(credentials, departmentId);
            personnel.setDepartment(department);
        }

        modelMapper.map(personnelRequest, personnel);

        return personnelRepository.save(personnel);
    }

    public Boolean addIdentification(
            AddIdentificationRequest addIdentification, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getIdentification() != null) {
            throw new ApiException(400, Exceptions.IDENTIFICATION_EXISTED);
        }

        Identification identification = modelMapper.map(addIdentification, Identification.class);
        identification = identificationRepository.save(identification);

        personnel.setIdentification(identification);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateIdentification(
            UpdateIdentificationRequest updateIdentification, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getIdentification() == null) {
            throw new ApiException(400, Exceptions.IDENTIFICATION_NOT_FOUND);
        }

        Identification identification = personnel.getIdentification();
        modelMapper.map(updateIdentification, identification);

        identificationRepository.save(identification);
        return true;
    }

    public Boolean addPassport(
            AddPassportRequest passportRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getPassport() != null) {
            throw new ApiException(400, Exceptions.PASSPORT_EXISTED);
        }

        Passport passport = modelMapper.map(passportRequest, Passport.class);
        passport = passportRepository.save(passport);

        personnel.setPassport(passport);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updatePassport(
            UpdatePassportRequest passportRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getPassport() == null) {
            throw new ApiException(400, Exceptions.PASSPORT_NOT_FOUND);
        }

        Passport passport = personnel.getPassport();
        modelMapper.map(passportRequest, passport);

        passportRepository.save(passport);
        return true;
    }

    public Boolean addWorkingTime(
            AddWorkingTimeRequest workingTimeRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getWorkingTime() != null) {
            throw new ApiException(400, Exceptions.WORKING_TIME_EXISTED);
        }

        WorkingTime workingTime = modelMapper.map(workingTimeRequest, WorkingTime.class);
        workingTime = workingTimeRepository.save(workingTime);

        personnel.setWorkingTime(workingTime);
        personnelRepository.save(personnel);
        return true;
    }

    public Boolean updateWorkingTime(
            UpdateWorkingTimeRequest workingTimeRequest, int personnelId, Credentials credentials
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        if (personnel.getWorkingTime() == null) {
            throw new ApiException(400, Exceptions.WORKING_TIME_NOT_FOUND);
        }

        WorkingTime workingTime = personnel.getWorkingTime();
        modelMapper.map(workingTimeRequest, workingTime);

        workingTimeRepository.save(workingTime);
        return true;
    }

    public Page<Personnel> findMany(Credentials credentials, FindAllPersonnelRequest findAllPersonnelRequest)
            throws ApiException {
        User.Status status = EnumUtil.getEnum(User.Status.class, findAllPersonnelRequest.getStatus());
        Gender gender = EnumUtil.getEnum(Gender.class, findAllPersonnelRequest.getGender());

        String createdBy = findAllPersonnelRequest.getCreatedBy();
        String lastModifiedBy = findAllPersonnelRequest.getLastModifiedBy();
        String username = findAllPersonnelRequest.getUsername();
        String address = findAllPersonnelRequest.getAddress();
        String fullName = findAllPersonnelRequest.getFullName();
        String identification = findAllPersonnelRequest.getIdentification();
        String phoneNumber = findAllPersonnelRequest.getPhoneNumber();
        String email = findAllPersonnelRequest.getEmail();
        String position = findAllPersonnelRequest.getPosition();
        String degree = findAllPersonnelRequest.getDegree();
        String description = findAllPersonnelRequest.getDescription();
        String department = findAllPersonnelRequest.getDepartment();
        List<Integer> userIds = findAllPersonnelRequest.getIds();

        Integer currentPage = findAllPersonnelRequest.getCurrentPage();
        Integer limit = findAllPersonnelRequest.getLimit();
        String sortDirection = findAllPersonnelRequest.getSortDirection();
        String sortBy = findAllPersonnelRequest.getSortBy();


        SpecificationBuilder<Personnel> builder = new SpecificationBuilder<>();
        builder
                .query("position", CONTAINS, position)
                .query("degree", CONTAINS, degree)
                .query("description", CONTAINS, description)
                .query("user.address", CONTAINS, address)
                .query("user.username", CONTAINS, username)
                .query("user.email", CONTAINS, email)
                .query("user.fullName", CONTAINS, fullName)
                .query("user.phoneNumber", CONTAINS, phoneNumber)
                .query("user.identification", CONTAINS, identification)
                .query("user.status", EQUALITY, status)
                .query("user.gender", EQUALITY, gender)
                .query("user.id", IN, userIds)
                .query("department.name", CONTAINS, department)
                .query("createdBy.username", CONTAINS, createdBy)
                .query("lastModifiedBy.username", CONTAINS, lastModifiedBy);

        Specification<Personnel> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(currentPage, limit, sortDirection, sortBy);

        return personnelRepository.findAll(spec, pageable);
    }
}
