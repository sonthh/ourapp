package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UpdateUserRequest {
    @ApiModelProperty()
    @Size(min = 6, max = 20)
    private String password;

    @ApiModelProperty()
    @Email
    @NotEmpty
    private String email;
}
