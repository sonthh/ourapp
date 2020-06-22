package com.son.repository;

import com.son.entity.Allowance;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AllowancesRepository extends CrudRepository<Allowance, Integer>,
    PagingAndSortingRepository<Allowance, Integer>,
    JpaSpecificationExecutor<Allowance> {
}
