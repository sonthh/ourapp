package com.son.repository;

import com.son.entity.Certification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CertificationRepository extends CrudRepository<Certification, Integer>,
    PagingAndSortingRepository<Certification, Integer>,
    JpaSpecificationExecutor<Certification> {
}
