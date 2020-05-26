package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddQualificationRequest {
    @ApiModelProperty
    @NotNull
    private String degree;

    @ApiModelProperty
    @NotNull
    private String major;

    @ApiModelProperty
    private String note;

    @ApiModelProperty
    private String trainedAt;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String issueDate;
}
