package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateCertificationRequest {
    @ApiModelProperty
    private String name;

    @ApiModelProperty
    private String type;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String issueDate;

    @ApiModelProperty
    private String issueAt;

    @ApiModelProperty
    private String note;
}
