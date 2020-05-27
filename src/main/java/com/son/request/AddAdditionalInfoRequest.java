package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddAdditionalInfoRequest {
    @ApiModelProperty
    private String ethnic;

    @ApiModelProperty
    private Boolean married;

    @ApiModelProperty
    private String note;

    @ApiModelProperty
    private String religion;
}
