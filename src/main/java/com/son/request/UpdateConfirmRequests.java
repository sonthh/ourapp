package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateConfirmRequests {
    @ApiModelProperty
    @NotNull
    private Integer requestsId;

    @ApiModelProperty
    @NotNull
    private String status;
}
