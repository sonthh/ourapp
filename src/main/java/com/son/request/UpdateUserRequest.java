package com.son.request;

import com.son.entity.User;
import com.son.model.Gender;
import com.son.validator.IsDateString;
import com.son.validator.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateUserRequest {
    @ApiModelProperty
    @Size(min = 6, max = 20)
    private String username;

    @ApiModelProperty
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

    @ApiModelProperty
    @IsEnum(enumClass = User.Status.class)
    private String status;

    @ApiModelProperty
    @UniqueElements
    private List<Integer> roleIds;
}
