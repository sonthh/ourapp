package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FirebaseTokenRequest {
    @ApiModelProperty(required = true)
    @NotBlank
    @NotNull
    private String token;
}
