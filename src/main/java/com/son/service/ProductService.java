package com.son.service;

import com.son.entity.User;
import com.son.request.CreateProductRequest;
import com.son.request.DeleteManyProductRequest;
import com.son.request.FindAllProductRequest;
import com.son.request.UpdateProductRequest;
import com.son.entity.Product;
import com.son.handler.ApiException;
import com.son.repository.ProductRepository;
import com.son.security.Credentials;
import com.son.util.page.PageUtil;
import com.son.util.spec.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.son.util.spec.SearchOperation.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

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

        return productRepository.save(savedProduct);
    }

    public Page<Product> findMany(FindAllProductRequest findAllProductRequest) {
        Integer price = findAllProductRequest.getPrice();
        String name = findAllProductRequest.getName();
        String status = findAllProductRequest.getStatus();
        Product.Status productStatus = status == null ? null : Product.Status.valueOf(status);
        List<Integer> productIds = findAllProductRequest.getIds();
        Integer currentPage = findAllProductRequest.getCurrentPage();
        Integer limit = findAllProductRequest.getLimit();
        String sortDirection = findAllProductRequest.getSortDirection();
        String sortBy = findAllProductRequest.getSortBy();
        String createdBy = findAllProductRequest.getCreatedBy();

        SpecificationBuilder<Product> builder = new SpecificationBuilder<>();
        builder
            .query("price", EQUALITY, price)
            .query("name", CONTAINS, name)
            .query("createdBy", CONTAINS, createdBy , "username")
            .query("status", EQUALITY, productStatus)
            .query("id", IN, productIds);

        Specification<Product> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(currentPage, limit, sortDirection, sortBy);

        return productRepository.findAll(spec, pageable);
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

        updatedProduct = productRepository.save(updatedProduct);

        return updatedProduct;
    }

    public Boolean deleteMany(Credentials credentials, DeleteManyProductRequest deleteManyProductRequest)
        throws ApiException {
        List<Integer> ids = deleteManyProductRequest.getIds();
        List<Product> products = (List<Product>) productRepository.findAllById(ids);

        if (products.size() != ids.size()) {
            throw new ApiException(404, "ProductNotFound");
        }

        productRepository.deleteAll(products);
        return true;
    }
}
