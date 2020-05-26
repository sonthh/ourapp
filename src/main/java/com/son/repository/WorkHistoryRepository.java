package com.son.repository;

import com.son.entity.WorkHistory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WorkHistoryRepository extends CrudRepository<WorkHistory, Integer>,
    PagingAndSortingRepository<WorkHistory, Integer>,
    JpaSpecificationExecutor<WorkHistory> {
}
