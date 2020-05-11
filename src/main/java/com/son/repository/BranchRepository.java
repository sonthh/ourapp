package com.son.repository;

import com.son.entity.Branch;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BranchRepository extends CrudRepository<Branch, Integer>,
        PagingAndSortingRepository<Branch, Integer>,
        JpaSpecificationExecutor<Branch> {
}
