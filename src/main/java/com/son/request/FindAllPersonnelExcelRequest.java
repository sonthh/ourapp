package com.son.request;

import com.son.model.Gender;
import com.son.model.SortDirection;
import com.son.validator.IsEnum;
import com.son.validator.IsNotNullItemInArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class FindAllPersonnelExcelRequest {
    enum SortBy {
        id, birthDay, lastModifiedDate, createdDate
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
    @IsEnum(enumClass = SortBy.class)
    private String sortBy = "id";

    @ApiModelProperty
    @IsEnum(enumClass = Gender.class)
    private String gender;

    @ApiModelProperty()
    @Size(min = 1)
    private String fullName;

    @ApiModelProperty()
    @Size(min = 1)
    private String createdBy;

    @ApiModelProperty()
    @Size(min = 1)
    private String lastModifiedBy;

    @ApiModelProperty()
    private String email;

    @ApiModelProperty()
    private Boolean isStopWork;

    @ApiModelProperty()
    @UniqueElements
    @IsNotNullItemInArray
    private List<Integer> ids;

    @ApiModelProperty()
    @Size(min = 1)
    private String position;

    @ApiModelProperty()
    private Integer departmentId;

    @ApiModelProperty(required = true)
    @NotEmpty
    @NotNull
    @UniqueElements
    @IsNotNullItemInArray
    private List<String> columns;
}
