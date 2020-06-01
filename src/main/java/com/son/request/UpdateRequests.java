package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateRequests {
    @ApiModelProperty
    private String type;

    @ApiModelProperty
    @NotNull
    private Integer receiverId;

    @ApiModelProperty
    private String info;

    @ApiModelProperty
    private String reason;
}
