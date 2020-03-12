package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @ApiModelProperty(required = true)
    @NotBlank
    @Size(min = 5)
    @NotNull
    private String username;

    @ApiModelProperty(required = true)
    @NotBlank
    @Size(min = 6)
    @NotNull
    private String password;
}
