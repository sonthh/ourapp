package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateDepartmentRequest {
    @ApiModelProperty
    @Size(min = 6, max = 20)
    private String name;

    @ApiModelProperty
    @Size(min = 6, max = 20)
    private String description;

    @ApiModelProperty
    private Integer branchId;
}
