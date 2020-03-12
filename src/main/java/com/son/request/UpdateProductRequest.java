package com.son.request;

import com.son.entity.ProductStatus;
import com.son.validator.ValueOfEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class UpdateProductRequest {
    @ApiModelProperty(dataType = "com.son.entity.ProductStatus")
    @ValueOfEnum(enumClass = ProductStatus.class)
    private String status;

    @ApiModelProperty()
    @Size(min = 1)
    private String name;

    @ApiModelProperty()
    @Min(10)
    private Integer price;
}
