package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAvatarRequest {
    @ApiModelProperty(required = true)
    @NotNull
    private MultipartFile avatar;
}
