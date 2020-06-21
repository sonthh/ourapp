package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddAllowancesRequest {
    @ApiModelProperty
    private String name;

    @ApiModelProperty
    private Double amount;
}
