package com.son.repository;

import com.son.entity.Passport;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PassportRepository extends CrudRepository<Passport, Integer>,
        PagingAndSortingRepository<Passport, Integer>,
        JpaSpecificationExecutor<Passport> {
}
