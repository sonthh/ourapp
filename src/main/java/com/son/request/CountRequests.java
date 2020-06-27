package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CountRequests {
    @ApiModelProperty()
    private String type;
}
