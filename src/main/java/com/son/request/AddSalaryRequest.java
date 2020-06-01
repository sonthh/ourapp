package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddSalaryRequest {
    @ApiModelProperty
    private Double baseSalary;

    @ApiModelProperty
    private Double coefficient;

    @ApiModelProperty
    private String payType;
}
