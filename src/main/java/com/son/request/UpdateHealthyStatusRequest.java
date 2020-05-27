package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateHealthyStatusRequest {
    @ApiModelProperty
    private String bloodGroup;

    @ApiModelProperty
    private String healthyStatus;

    @ApiModelProperty
    private Double height;

    @ApiModelProperty
    private String lastCheckDate;

    @ApiModelProperty
    private Double weight;
}
