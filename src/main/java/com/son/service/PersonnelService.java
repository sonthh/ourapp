package com.son.service;

import com.son.entity.Department;
import com.son.entity.Personnel;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.repository.DepartmentRepository;
import com.son.repository.PersonnelRepository;
import com.son.repository.UserRepository;
import com.son.request.UpdatePersonnelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonnelService {
    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    public Boolean isDeletedOne(int personnelId) {
        Optional<Personnel> personnel = personnelRepository.findById(personnelId);
        if (!personnel.isPresent()) {
            return false;
        }
        personnelRepository.deleteById(personnelId);
        return true;
    }

    public Personnel updateOne(UpdatePersonnelRequest updatePersonnelRequest, int id) throws ApiException {
        Optional<Personnel> oldPersonnel = personnelRepository.findById(id);
        Optional<Department> department = departmentRepository.findById(updatePersonnelRequest.getDepartmentId());
        Optional<User> user = userRepository.findById(updatePersonnelRequest.getUserId());

        if (!oldPersonnel.isPresent()) {
            throw new ApiException(404, "PersonnelIdNotFound");
        }

        if (!department.isPresent()) {
            throw new ApiException(404, "DepartmentIdNotFound");
        }

        if (!user.isPresent()) {
            throw new ApiException(404, "UserIdNotFound");
        }

        Personnel updatePersonnel = oldPersonnel.get();
        updatePersonnel.setDegree(updatePersonnelRequest.getDegree());
        updatePersonnel.setDescription(updatePersonnelRequest.getDescription());
        updatePersonnel.setPosition(updatePersonnelRequest.getPosition());
        updatePersonnel.setDepartment(department.get());
        updatePersonnel.setUser(user.get());

        return personnelRepository.save(updatePersonnel);
    }
}
