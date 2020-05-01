package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdatePersonnelRequest {
    @ApiModelProperty
    @NotNull
    private String degree;

    @ApiModelProperty
    private String description;

    @ApiModelProperty
    @NotNull
    private String position;

    @ApiModelProperty
    private int departmentId;

    @ApiModelProperty(required = true)
    @NotNull
    private int userId;
}
