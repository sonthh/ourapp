package com.son.service;

import com.son.entity.Personnel;
import com.son.repository.PersonnelRepository;
import com.son.repository.UserRepository;
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
    private UserRepository userRepository;

    public Boolean isDeletedOne(int personnelId) {
        Optional<Personnel> personnel = personnelRepository.findById(personnelId);
        if (!personnel.isPresent()) {
            return false;
        }
        personnelRepository.deleteById(personnelId);
        return true;
    }
}
