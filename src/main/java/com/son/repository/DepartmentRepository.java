package com.son.repository;

import com.son.entity.Department;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DepartmentRepository extends CrudRepository<Department, Integer>,
    PagingAndSortingRepository<Department, Integer>,
    JpaSpecificationExecutor<Department> {
}
