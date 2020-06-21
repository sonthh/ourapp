package com.son.repository;

import com.son.entity.Requests;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RequestsRepository extends CrudRepository<Requests, Integer>,
    PagingAndSortingRepository<Requests, Integer>,
    JpaSpecificationExecutor<Requests> {
}
