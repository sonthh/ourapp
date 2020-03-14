package com.son.request;

import com.son.entity.NotificationType;
import com.son.validator.IsEnumArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NotificationTypesRequest {
    @ApiModelProperty(required = true)
    @NotEmpty
    @NotNull
    @IsEnumArray(enumClass = NotificationType.class)
    @UniqueElements
    private List<String> notificationTypes;
}
