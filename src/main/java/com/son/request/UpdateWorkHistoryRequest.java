package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateWorkHistoryRequest {
    @ApiModelProperty
    @NotNull
    private String workAt;

    @ApiModelProperty
    @NotNull
    private String role;

    @ApiModelProperty
    @NotNull
    private Integer workTime;

    @ApiModelProperty
    @NotNull
    @IsDateString(pattern = "yyyy-MM-dd")
    private String startDate;

    @ApiModelProperty
    @NotNull
    @IsDateString(pattern = "yyyy-MM-dd")
    private String endDate;
}
