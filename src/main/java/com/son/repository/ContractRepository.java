package com.son.repository;

import com.son.entity.Contract;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ContractRepository extends CrudRepository<Contract, Integer>,
    PagingAndSortingRepository<Contract, Integer>,
    JpaSpecificationExecutor<Contract> {

    Optional<Contract> findByContractNumber(String contractNumber);
}
