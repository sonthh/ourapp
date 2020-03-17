package com.son.repository;

import com.son.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer>,
    PagingAndSortingRepository<User, Integer>,
    JpaSpecificationExecutor<User> {

    Optional<User> findOneByUsername(@Param("username") String username);

    @Query("select u from User u where u.username = :username and u.status = :status")
    Optional<User> findActiveUser(String username, User.Status status);
}