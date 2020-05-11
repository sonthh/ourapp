package com.son.repository;

import com.son.entity.Department;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, Integer>,
    PagingAndSortingRepository<Department, Integer>,
    JpaSpecificationExecutor<Department> {

    Optional<Department> findOneByName(String name);
}
