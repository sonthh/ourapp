package com.son.request;

import com.son.entity.Product;
import com.son.validator.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateProductRequest {
    @ApiModelProperty(required = true)
    @NotBlank
    @NotNull
    private String name;

    @ApiModelProperty(required = true)
    @NotNull
    @Min(10)
    private Integer price;

    @ApiModelProperty(required = true, dataType = "com.son.entity.Product.Status")
    @IsEnum(enumClass = Product.Status.class)
    @NotNull
    private String status;
}
