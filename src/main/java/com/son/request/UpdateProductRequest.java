package com.son.request;

import com.son.entity.Product;
import com.son.validator.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class UpdateProductRequest {
    @ApiModelProperty(dataType = "com.son.entity.Product.Status")
    @IsEnum(enumClass = Product.Status.class)
    private String status;

    @ApiModelProperty()
    @Size(min = 1)
    private String name;

    @ApiModelProperty()
    @Min(10)
    private Integer price;
}
