package com.son.service;

import com.son.constant.Exceptions;
import com.son.entity.Department;
import com.son.entity.Personnel;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.model.Gender;
import com.son.repository.DepartmentRepository;
import com.son.repository.PersonnelRepository;
import com.son.repository.UserRepository;
import com.son.request.CreatePersonnelRequest;
import com.son.request.DeleteManyByIdRequest;
import com.son.request.FindAllPersonnelRequest;
import com.son.request.UpdatePersonnelBasicInfo;
import com.son.security.Credentials;
import com.son.util.common.EnumUtil;
import com.son.util.page.PageUtil;
import com.son.util.spec.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

//    @Autowired
//    public PersonnelService(ModelMapper modelMapper) {
//        this.modelMapper = modelMapper;
//
//        this.modelMapper.addMappings(new PropertyMap<UpdatePersonnelRequest, Personnel>() {
//            @SneakyThrows
//            @Override
//            protected void configure() {
//                skip(destination.getId());
//
//                map().setDegree(source.getDegree());
//                map().setPosition(source.getPosition());
//                map().setDescription(source.getDescription());
//
//                map().getDepartment().setId(source.getDepartmentId());
//
//                map().getUser().setId(source.getUserId());
//                map().getUser().setFullName(source.getFullName());
//                map().getUser().setAddress(source.getAddress());
//                map().getUser().setPhoneNumber(source.getPhoneNumber());
//                map().getUser().setEmail(source.getEmail());
//            }
//        });
//
//        this.modelMapper.addMappings(new PropertyMap<CreatePersonnelRequest, Personnel>() {
//            @Override
//            protected void configure() {
//                skip(destination.getId());
//                skip(destination.getDepartment().getId());
//                skip(destination.getUser().getId());
//
//                map().setDegree(source.getDegree());
//                map().setPosition(source.getPosition());
//                map().setDescription(source.getDescription());
//
//                map().getUser().setFullName(source.getFullName());
//                map().getUser().setAddress(source.getAddress());
//                map().getUser().setPhoneNumber(source.getPhoneNumber());
//                map().getUser().setEmail(source.getEmail());
//            }
//        });
//    }

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
            UpdatePersonnelBasicInfo personnelRequest, int personnelId
    ) throws ApiException {
        Personnel personnel = findOne(personnelId);

        Integer departmentId = personnelRequest.getDepartmentId();
        if (departmentId != null) {
            Optional<Department> opDepartment = departmentRepository.findById(departmentId);

            if (!opDepartment.isPresent()) {
                throw new ApiException(404, Exceptions.DEPARTMENT_NOT_FOUND);
            }


        }
//        if (!department.isPresent()) {
//            throw new ApiException(404, Exceptions.DEPARTMENT_NOT_FOUND);
//        }
//
//        if (!user.isPresent()) {
//            throw new ApiException(404, Exceptions.USER_NOT_FOUND);
//        }
//
//        if (checkPersonnelByUserID.isPresent()
//            && !checkPersonnelByUserID.get().getId().equals(oldPersonnel.get().getId())) {
//            throw new ApiException(404, Exceptions.USER_EXIST);
//        }
//
//        Personnel updatePersonnel = oldPersonnel.get();
//
//        modelMapper.map(updatePersonnelRequest, updatePersonnel);
//
//        if (updatePersonnelRequest.getBirthDay() != null) {
//            updatePersonnel.getUser().setBirthDay(new SimpleDateFormat("yyyy-MM-dd")
//                .parse(updatePersonnelRequest.getBirthDay()));
//        }
//
//        if (updatePersonnelRequest.getGender() != null) {
//            updatePersonnel.getUser().setGender(Gender.valueOf(updatePersonnelRequest.getGender()));
//        }
//
//        return personnelRepository.save(updatePersonnel);
        return null;
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
