package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateWorkHistoryRequest {
    @ApiModelProperty
    private String workAt;

    @ApiModelProperty
    private String role;

    @ApiModelProperty
    private Integer workTime;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String startDate;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String endDate;
}
