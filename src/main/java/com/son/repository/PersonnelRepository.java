package com.son.repository;

import com.son.entity.Personnel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonnelRepository extends CrudRepository<Personnel, Integer>,
    PagingAndSortingRepository<Personnel, Integer>,
    JpaSpecificationExecutor<Personnel> {

    @Query(value = "select p from Personnel AS p where p.user.id = :userId")
    Optional<Personnel> findOneByUserId(@Param("userId") int userId);
}
