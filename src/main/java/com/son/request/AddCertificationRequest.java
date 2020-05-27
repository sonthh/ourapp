package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddCertificationRequest {
    @ApiModelProperty
    @NotNull
    private String name;

    @ApiModelProperty
    @NotNull
    private String type;

    @ApiModelProperty
    @NotNull
    @IsDateString(pattern = "yyyy-MM-dd")
    private String issueDate;

    @ApiModelProperty
    @NotNull
    private String issueAt;

    @ApiModelProperty
    @NotNull
    private String note;
}
