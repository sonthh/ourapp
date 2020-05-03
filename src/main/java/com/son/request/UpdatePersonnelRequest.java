package com.son.request;

import com.son.model.Gender;
import com.son.validator.IsDateString;
import com.son.validator.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdatePersonnelRequest {
    @ApiModelProperty
    @NotNull
    private String degree;

    @ApiModelProperty
    private String description;

    @ApiModelProperty
    @NotNull
    private String position;

    @ApiModelProperty
    private int departmentId;

    @ApiModelProperty(required = true)
    @NotNull
    private int userId;

    @ApiModelProperty
    @Size(min = 6, max = 20)
    private String fullName;

    @ApiModelProperty
    private String address;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String birthDay;

    @ApiModelProperty
    @Email
    private String email;

    @ApiModelProperty
    private String phoneNumber;

    @ApiModelProperty
    @IsEnum(enumClass = Gender.class)
    private String gender;
}
