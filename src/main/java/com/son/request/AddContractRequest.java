package com.son.request;

import com.son.validator.IsDateString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddContractRequest {
    @ApiModelProperty
    @NotNull
    private Integer personnelId;

    @ApiModelProperty
    @NotNull
    private String contractType;

    @ApiModelProperty
    @NotNull
    private String taxType;

    @ApiModelProperty
    @NotNull
    @IsDateString(pattern = "yyyy-MM-dd")
    private String validDate;

    @ApiModelProperty
    @NotNull
    private Integer signerId;

    @ApiModelProperty
    @NotNull
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
