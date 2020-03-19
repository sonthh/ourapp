package com.son.request;

import com.son.entity.Product;
import com.son.model.SortDirection;
import com.son.validator.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class FindAllProductRequest {
    enum SortBy {
        id, name, price
    }

    @ApiModelProperty
    @Min(0)
    private Integer offset = 0;

    @ApiModelProperty
    @Min(1)
    private Integer limit = 10;

    @ApiModelProperty()
    @IsEnum(enumClass = SortDirection.class)
    private String sortDirection = "DESC";

    @ApiModelProperty()
    @IsEnum(enumClass = SortBy.class)
    private String sortBy = "id";

    @ApiModelProperty(dataType = "com.son.entity.Product.Status")
    @IsEnum(enumClass = Product.Status.class)
    private String status;

    @ApiModelProperty()
    @Size(min = 1)
    private String name;

    @ApiModelProperty()
    @Min(10)
    private Integer price;

    @ApiModelProperty()
    private List<Integer> ids;
}
