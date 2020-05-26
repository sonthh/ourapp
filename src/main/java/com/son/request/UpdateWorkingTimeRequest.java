package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateWorkingTimeRequest {
    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String startWorkDate;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String signContractDate;

    @ApiModelProperty
    private String workType;

    @ApiModelProperty
    private String note;

    @ApiModelProperty
    private Boolean isStopWork;

    @ApiModelProperty
    private String reasonStopWork;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String stopWorkDate;
}
