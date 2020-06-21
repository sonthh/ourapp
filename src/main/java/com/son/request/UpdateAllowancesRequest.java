package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateAllowancesRequest {
    @ApiModelProperty
    private String name;

    @ApiModelProperty
    private Double amount;
}
