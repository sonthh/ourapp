package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateContractRequest {
    @ApiModelProperty
    private Integer personnelId;

    @ApiModelProperty
    private String contractType;

    @ApiModelProperty
    private String taxType;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String validDate;

    @ApiModelProperty
    private Integer signerId;

    @ApiModelProperty
    private String workType;

    @ApiModelProperty
    private String contractNumber;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String expiredDate;

    @ApiModelProperty
    private Integer probationTime;

    @ApiModelProperty
    private Integer salary;

    @ApiModelProperty
    private String workAt;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String startWorkDate;

    @ApiModelProperty
    private String note;
}
