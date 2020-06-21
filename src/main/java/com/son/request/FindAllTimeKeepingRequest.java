package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class FindAllTimeKeepingRequest {
    @ApiModelProperty()
    @Size(min = 1)
    private String fullName;

    @ApiModelProperty()
    private Integer departmentId;

    @ApiModelProperty()
    private List<String> dates;
}
