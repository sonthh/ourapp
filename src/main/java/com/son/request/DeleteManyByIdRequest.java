package com.son.request;

import com.son.validator.IsNotNullItemInArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeleteManyByIdRequest {
    @ApiModelProperty(required = true)
    @NotEmpty
    @NotNull
    @UniqueElements
    @IsNotNullItemInArray
    private List<Integer> ids;
}
