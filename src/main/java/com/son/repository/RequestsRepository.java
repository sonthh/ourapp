package com.son.repository;

import com.son.entity.Request;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RequestsRepository extends CrudRepository<Request, Integer>,
    PagingAndSortingRepository<Request, Integer>,
    JpaSpecificationExecutor<Request> {
}
