package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FindSalaryRequest {
    @ApiModelProperty()
    private Integer departmentId;

    @ApiModelProperty()
    private List<String> dates;
}
