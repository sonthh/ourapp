package com.son.repository;

import com.son.entity.Personnel;
import com.son.entity.TimeKeeping;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface TimeKeepingRepository extends CrudRepository<TimeKeeping, Integer>,
        PagingAndSortingRepository<TimeKeeping, Integer>,
        JpaSpecificationExecutor<TimeKeeping> {

    List<TimeKeeping> findByDateBetween(Date endDate, Date startDate);
    TimeKeeping findByDateAndPersonnel(Date date, Personnel personnel);
}
