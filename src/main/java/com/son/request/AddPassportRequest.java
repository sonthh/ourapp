package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddPassportRequest {
    @ApiModelProperty
    @NotNull
    private String issueAt;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    @NotNull
    private String issueDate;

    @ApiModelProperty
    @NotNull
    private String number;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    @NotNull
    private String expiredDate;
}
