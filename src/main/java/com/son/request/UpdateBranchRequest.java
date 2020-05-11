package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateBranchRequest {
    @ApiModelProperty
    @Size(min = 6, max = 20)
    private String name;

    @ApiModelProperty
    @Size(min = 6, max = 20)
    private String location;

    @ApiModelProperty
    @Size(min = 6, max = 20)
    private String description;
}
