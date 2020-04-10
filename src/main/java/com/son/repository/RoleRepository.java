package com.son.repository;

import com.son.entity.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer>,
        PagingAndSortingRepository<Role, Integer>,
        JpaSpecificationExecutor<Role> {

    Optional<Role> findOneByName(@Param("name") String name);
}
