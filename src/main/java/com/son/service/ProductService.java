package com.son.service;

import com.son.entity.User;
import com.son.request.CreateProductRequest;
import com.son.request.FindAllProductRequest;
import com.son.request.UpdateProductRequest;
import com.son.entity.Product;
import com.son.entity.ProductStatus;
import com.son.handler.ApiException;
import com.son.repository.ProductRepository;
import com.son.security.Credentials;
import com.son.util.page.PageUtil;
import com.son.util.spec.SearchOperation;
import com.son.util.spec.SpecificationBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    private void validatePermission(Credentials credentials, Product product) throws ApiException {
        if (product == null) {
            return;
        }

        // is admin account
        if (credentials.isAdmin()) {
            return;
        }

        User createdBy = product.getCreatedBy();
        // is owner
        if (createdBy != null && createdBy.sameCredentials(credentials)) {
            return;
        }

        throw new ApiException(403, "NoPermission");
    }

    public Product createOne(CreateProductRequest createProductRequest) {
        Product savedProduct = modelMapper.map(createProductRequest, Product.class);

        // validate access authorization

        Product newProduct = productRepository.save(savedProduct);

        return newProduct;
    }

    public Page<Product> findMany(FindAllProductRequest findAllProductRequest) {
        Integer price = findAllProductRequest.getPrice();
        String name = findAllProductRequest.getName();
        String status = findAllProductRequest.getStatus();
        List<Integer> productIds = findAllProductRequest.getIds();
        Integer offset = findAllProductRequest.getOffset();
        Integer limit = findAllProductRequest.getLimit();
        String sortDirection = findAllProductRequest.getSortDirection();
        String sortBy = findAllProductRequest.getSortBy();

        SpecificationBuilder<Product> builder = new SpecificationBuilder<>();
        builder
            .query("price", SearchOperation.EQUALITY, price)
            .query("name", SearchOperation.EQUALITY, name, "*", "*")
            .query("status", SearchOperation.EQUALITY, ProductStatus.valueOf(status))
            .query("id", SearchOperation.IN, productIds);

        Specification<Product> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(offset, limit, sortDirection, sortBy);

        Page<Product> page = productRepository.findAll(spec, pageable);

        return page;
    }

    public Product findOne(Credentials credentials, Integer productId) throws ApiException {
        Optional<Product> optional = productRepository.findById(productId);

        if (!optional.isPresent()) {
            throw new ApiException(404, "ProductNotFound");
        }

        // validate access authorization

        return optional.get();
    }

    public Boolean deleteOne(Credentials credentials, Integer productId) throws ApiException {
        Optional<Product> optional = productRepository.findById(productId);

        if (!optional.isPresent()) {
            throw new ApiException(400, "ProductNotFound");
        }

        Product product = optional.get();

        validatePermission(credentials, product);

        productRepository.deleteById(product.getId());

        return true;
    }

    public Product updateOne(UpdateProductRequest updateProductRequest, Integer productId) throws ApiException {
        Optional<Product> optional = productRepository.findById(productId);

        if (!optional.isPresent()) {
            throw new ApiException(404, "ProductNotFound");
        }

        Product updatedProduct = optional.get();
        modelMapper.map(updateProductRequest, updatedProduct);

        // validate access authorization

        productRepository.save(updatedProduct);

        return updatedProduct;
    }
}
