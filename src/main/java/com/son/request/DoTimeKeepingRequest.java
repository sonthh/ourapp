package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DoTimeKeepingRequest {
    @ApiModelProperty
    @NotNull
    private String status;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    @NotNull
    private String date;

    @ApiModelProperty
    private Integer requestId;
}
