package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateAdditionalInfoRequest {
    @ApiModelProperty
    private String ethnic;

    @ApiModelProperty
    private Boolean married;

    @ApiModelProperty
    private String note;

    @ApiModelProperty
    private String religion;
}
