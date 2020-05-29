package com.son.repository;

import com.son.entity.AdditionalInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AdditionalInfoRepository extends CrudRepository<AdditionalInfo, Integer>,
    PagingAndSortingRepository<AdditionalInfo, Integer>,
    JpaSpecificationExecutor<AdditionalInfo> {
}
