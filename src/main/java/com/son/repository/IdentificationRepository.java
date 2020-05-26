package com.son.repository;

import com.son.entity.Identification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IdentificationRepository extends CrudRepository<Identification, Integer>,
        PagingAndSortingRepository<Identification, Integer>,
        JpaSpecificationExecutor<Identification> {
}
