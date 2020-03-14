package com.son.service;

import com.son.request.CreateProductRequest;
import com.son.request.FindAllProductRequest;
import com.son.request.UpdateProductRequest;
import com.son.entity.Product;
import com.son.entity.ProductStatus;
import com.son.handler.ApiException;
import com.son.repository.ProductRepository;
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

    public Product createOne(CreateProductRequest createProductRequest) {
        Product savedProduct = modelMapper.map(createProductRequest, Product.class);

        // validate access authorization

        Product newProduct = productRepository.save(savedProduct);

        return newProduct;
    }

    public Page<Product> findMany(FindAllProductRequest findAllProductRequest) {
        SpecificationBuilder<Product> builder = new SpecificationBuilder<>();

        Integer price = findAllProductRequest.getPrice();
        String name = findAllProductRequest.getName();
        String status = findAllProductRequest.getStatus();
        List<Integer> productIds = findAllProductRequest.getIds();

        if (price != null) {
            builder.with("price", SearchOperation.EQUALITY, price);
        }

        if (name != null) {
            builder.with("name", SearchOperation.EQUALITY, name, "*", "*");
        }

        if (status != null) {
            builder.with("status", SearchOperation.EQUALITY, ProductStatus.valueOf(status));
        }

        if (productIds != null) {
            builder.with("id", SearchOperation.IN, productIds);
        }

        Specification<Product> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(
            findAllProductRequest.getOffset(),
            findAllProductRequest.getLimit(),
            findAllProductRequest.getSortDirection(),
            findAllProductRequest.getSortBy());

        Page<Product> page = productRepository.findAll(spec, pageable);

        return page;
    }

    public Product findOne(Integer productId) throws ApiException {
        Optional<Product> optional = productRepository.findById(productId);

        if (!optional.isPresent()) {
            throw new ApiException(404, "ProductNotFound");
        }

        // validate access authorization

        return optional.get();
    }

    public Boolean deleteOne(Integer productId) throws ApiException {
        Optional<Product> optional = productRepository.findById(productId);

        if (!optional.isPresent()) {
            throw new ApiException(400, "ProductNotFound");
        }

        // validate access authorization

        productRepository.deleteById(optional.get().getId());

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
