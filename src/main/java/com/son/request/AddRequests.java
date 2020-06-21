package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddRequests {
    @ApiModelProperty
    @NotNull
    private String type;

    @ApiModelProperty
    @NotNull
    private Integer receiverId;

    @ApiModelProperty
    private String info;

    @ApiModelProperty
    @NotNull
    private String reason;
}
