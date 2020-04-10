package com.son.controller;

import com.son.entity.Product;
import com.son.handler.ApiException;
import com.son.request.CreateProductRequest;
import com.son.request.DeleteManyProductRequest;
import com.son.request.FindAllProductRequest;
import com.son.request.UpdateProductRequest;
import com.son.security.Credentials;
import com.son.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Api(tags = "Products", value = "Product Controller")
@RestController
@RequestMapping("products")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ApiOperation("create one product")
    @PostMapping
    public ResponseEntity<Product> createOne(
        @Valid @RequestBody CreateProductRequest createProductRequest
    ) {
        Product newProduct = productService.createOne(createProductRequest);

        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @ApiOperation("find many product")
    @GetMapping
    @PreAuthorize("hasAnyAuthority(@scopes.PER_PRODUCT_READ, @scopes.DEPARTMENT_PRODUCT_DELETE)")
    public ResponseEntity<Page<Product>> findMany(
        @Valid FindAllProductRequest findAllProductRequest,
        @ApiIgnore BindingResult errors,
        @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) {
        Page<Product> page = productService.findMany(findAllProductRequest);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation("find one product")
    @GetMapping("/{productId}")
    public ResponseEntity<Product> findOne(
        @Min(1) @PathVariable Integer productId,
        @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Product product = productService.findOne(credentials, productId);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @ApiOperation("delete one product")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteOne(
        @Min(1) @PathVariable Integer productId,
        @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Boolean isDeleted = productService.deleteOne(credentials, productId);

        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    @ApiOperation("delete many product")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteMany(
        @Valid @RequestBody DeleteManyProductRequest deleteManyProductRequest,
        @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Boolean isDeleted = productService.deleteMany(credentials, deleteManyProductRequest);

        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    @ApiOperation("update one product")
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateOne(
        @Valid @RequestBody UpdateProductRequest updateProductRequest,
        @Min(1) @PathVariable Integer productId
    ) throws ApiException {
        Product updatedProduct = productService.updateOne(updateProductRequest, productId);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
