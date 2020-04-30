package com.son.repository;

import com.son.entity.Personnel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonnelRepository extends CrudRepository<Personnel, Integer>,
    PagingAndSortingRepository<Personnel, Integer>,
    JpaSpecificationExecutor<Personnel> {
}
