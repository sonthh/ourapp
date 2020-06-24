package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddRequest {
    @ApiModelProperty
    @NotNull
    private String type;

    @ApiModelProperty
    private Integer receiverId;

    @ApiModelProperty
    private Integer personnelId;

    @ApiModelProperty
    private String info;

    @ApiModelProperty
    private String reason;

    @ApiModelProperty
    @NotNull
    private String status;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String startDate;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String endDate;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String decidedDate;

    @ApiModelProperty
    private Double amount;
}
