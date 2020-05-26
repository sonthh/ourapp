package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateIdentificationRequest {
    @ApiModelProperty
    private String issueAt;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String issueDate;

    @ApiModelProperty
    private String number;
}
