package com.son.repository;

import com.son.entity.HealthyStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HealthyStatusRepository extends CrudRepository<HealthyStatus, Integer>,
    PagingAndSortingRepository<HealthyStatus, Integer>,
    JpaSpecificationExecutor<HealthyStatus> {
}
