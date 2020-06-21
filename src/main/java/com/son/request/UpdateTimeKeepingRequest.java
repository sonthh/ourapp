package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateTimeKeepingRequest {
    @ApiModelProperty
    private String status;

    @ApiModelProperty
    private Integer requestId;
}
