package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateBankInfoRequest {
    @ApiModelProperty
    private String accNumber;

    @ApiModelProperty
    private String bank;

    @ApiModelProperty
    private String branch;

    @ApiModelProperty
    private String owner;
}
