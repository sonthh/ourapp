package com.son.repository;

import com.son.entity.Salary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SalaryRepository extends CrudRepository<Salary, Integer>,
    PagingAndSortingRepository<Salary, Integer>,
    JpaSpecificationExecutor<Salary> {
}
