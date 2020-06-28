package com.son.request;

import com.son.model.SortDirection;
import com.son.validator.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class FindAllRequests {
    enum SortBy {
        id, lastModifiedDate, createdDate
    }

    @ApiModelProperty
    @Min(1)
    private Integer currentPage = 1;

    @ApiModelProperty
    @Min(1)
    private Integer limit = 10;

    @ApiModelProperty()
    @IsEnum(enumClass = SortDirection.class)
    private String sortDirection = "DESC";

    @ApiModelProperty()
    @IsEnum(enumClass = FindAllRequests.SortBy.class)
    private String sortBy = "id";

    @ApiModelProperty()
    @Size(min = 1)
    private String createdBy;

    @ApiModelProperty()
    @Size(min = 1)
    private String lastModifiedBy;

    @ApiModelProperty()
    private List<Integer> ids;

    @ApiModelProperty(hidden = true)
    private List<Date> decidedDates;

    @ApiModelProperty
    private Integer personnelId;

    @ApiModelProperty()
    @Size(min = 1)
    private String type;

    @ApiModelProperty()
    @Size(min = 1)
    private String reason;

    @ApiModelProperty()
    @Size(min = 1)
    private String status;

    @ApiModelProperty()
    private String receiver;
}
