package com.son.repository;

import com.son.entity.BankInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BankInfoRepository extends CrudRepository<BankInfo, Integer>,
    PagingAndSortingRepository<BankInfo, Integer>,
    JpaSpecificationExecutor<BankInfo> {
}
