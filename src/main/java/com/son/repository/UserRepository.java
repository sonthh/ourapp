package com.son.repository;

import com.son.entity.User;
import com.son.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository
        extends CrudRepository<User, Integer>,
        PagingAndSortingRepository<User, Integer>,
        JpaSpecificationExecutor<User> {

    User findOneByUsername(@Param("username") String username);

    @Query("select u from User u where u.username = :username and u.status = :status")
    User findActiveUser(String username, UserStatus status);
}