package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateQualificationRequest {
    @ApiModelProperty
    private String degree;

    @ApiModelProperty
    private String major;

    @ApiModelProperty
    private String note;

    @ApiModelProperty
    private String trainedAt;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String issueDate;
}
