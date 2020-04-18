package com.son.request;

import com.son.model.Gender;
import com.son.validator.IsDateString;
import com.son.validator.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UpdateUserMeRequest {
    @ApiModelProperty(required = true)
    @Size(min = 6, max = 20)
    private String password;

    @ApiModelProperty
    @Size(min = 6, max = 20)
    private String fullName;

    @ApiModelProperty
    @Email
    private String email;

    @ApiModelProperty
    private String phoneNumber;

    @ApiModelProperty
    @IsDateString(pattern = "yyyy-MM-dd")
    private String birthDay;

    @ApiModelProperty
    @IsEnum(enumClass = Gender.class)
    private String gender;

    @ApiModelProperty
    private String identification;

    @ApiModelProperty
    private String address;
}
