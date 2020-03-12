package com.son.repository;

import com.son.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository
        extends CrudRepository<Product, Integer>,
        PagingAndSortingRepository<Product, Integer>,
        JpaSpecificationExecutor<Product> {

}
