package com.son.repository;

import com.son.entity.TimeKeeping;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TimeKeepingRepository extends CrudRepository<TimeKeeping, Integer>,
        PagingAndSortingRepository<TimeKeeping, Integer>,
        JpaSpecificationExecutor<TimeKeeping> {
}
