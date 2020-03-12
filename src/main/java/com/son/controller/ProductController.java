package com.son.controller;

import com.son.request.CreateProductRequest;
import com.son.request.FindAllProductRequest;
import com.son.request.UpdateProductRequest;
import com.son.entity.Product;
import com.son.handler.ApiException;
import com.son.security.UserDetailsImpl;
import com.son.service.EmailService;
import com.son.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import javax.validation.constraints.NotNull;

@Api(tags = "Products", value = "Product Controller")
@RestController
@RequestMapping("products")
@Validated
public class ProductController {
    private final ProductService productService;
    private final EmailService emailService;

    public ProductController(ProductService productService, EmailService emailService) {
        this.productService = productService;
        this.emailService = emailService;
    }

    @ApiOperation("test send email")
    @GetMapping("testSendEmail")
    public ResponseEntity<Object> testSendEmail() {
        Thread thread = new Thread(() -> {
            emailService.sendWelcomeMail();
        });
        thread.start();

        return new ResponseEntity<>("Email will be sent in few seconds.", HttpStatus.CREATED);
    }

    @ApiOperation("create one product")
    @PostMapping
    public ResponseEntity<Product> createOne(
            @Valid @NotNull @RequestBody(required = false) CreateProductRequest createProductRequest) {

        Product newProduct = productService.createOne(createProductRequest);

        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @ApiOperation("find many product")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<Product>> findMany(
            @Valid FindAllProductRequest findAllProductRequest,
            @ApiIgnore BindingResult errors) {
        Page<Product> page = productService.findMany(findAllProductRequest);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation("find one product")
    @GetMapping("/{productId}")
    public ResponseEntity<Object> findOne(@Min(1) @PathVariable Integer productId) throws ApiException {
        Product product = productService.findOne(productId);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @ApiOperation("delete one product")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteOne(@Min(1) @PathVariable Integer productId) throws ApiException {
        Boolean isDeleted = productService.deleteOne(productId);

        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    @ApiOperation("update one product")
    @PutMapping("/{productId}")
    public ResponseEntity<Object> updateOne(
            @Valid @RequestBody UpdateProductRequest updateProductRequest,
            @Min(1) @PathVariable Integer productId
    ) throws ApiException {
        Product updatedProduct = productService.updateOne(updateProductRequest, productId);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

}