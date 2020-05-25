package com.son.request;


import com.son.model.Gender;
import com.son.validator.IsDateString;
import com.son.validator.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreatePersonnelRequest {
    @ApiModelProperty
    @Size(min = 6, max = 20)
    @NotNull
    private String fullName;

    @ApiModelProperty
    @NotNull
    private String phoneNumber;

    @ApiModelProperty
    @Email
    private String email;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String birthDay;

    @ApiModelProperty
    @IsEnum(enumClass = Gender.class)
    private String gender;

    @ApiModelProperty
    @NotNull
    @Min(1)
    private Integer departmentId;

    @ApiModelProperty
    private String position;
}
