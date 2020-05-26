package com.son.repository;

import com.son.entity.Qualification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QualificationRepository extends CrudRepository<Qualification, Integer>,
        PagingAndSortingRepository<Qualification, Integer>,
        JpaSpecificationExecutor<Qualification> {
}
