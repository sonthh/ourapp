package com.son.request;

import com.son.entity.User;
import com.son.model.Gender;
import com.son.validator.IsDateString;
import com.son.validator.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class CreateUserRequest {
    @ApiModelProperty(required = true)
    @Size(min = 6, max = 20)
    @NotNull
    private String username;

    @ApiModelProperty(required = true)
    @Size(min = 6, max = 20)
    @NotNull
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

    @ApiModelProperty(required = true)
    @IsEnum(enumClass = User.Status.class)
    @NotNull
    private String status;

    @ApiModelProperty(required = true)
    @NotEmpty
    @NotNull
    @UniqueElements
    private List<Integer> roleIds;
}
