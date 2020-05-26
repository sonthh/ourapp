package com.son.repository;

import com.son.entity.WorkingTime;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WorkingTimeRepository extends CrudRepository<WorkingTime, Integer>,
        PagingAndSortingRepository<WorkingTime, Integer>,
        JpaSpecificationExecutor<WorkingTime> {
}
