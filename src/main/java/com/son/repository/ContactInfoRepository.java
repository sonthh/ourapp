package com.son.repository;

import com.son.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContactInfoRepository extends CrudRepository<ContactInfo, Integer>,
    PagingAndSortingRepository<ContactInfo, Integer>,
    JpaSpecificationExecutor<ContactInfo> {
}
