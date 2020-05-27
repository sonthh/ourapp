package com.son.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateContactInfoRequest {
    @ApiModelProperty
    private String address;

    @ApiModelProperty
    private String district;

    @ApiModelProperty
    private String facebook;

    @ApiModelProperty
    private String homeNumber;

    @ApiModelProperty
    private String province;

    @ApiModelProperty
    private String relation;

    @ApiModelProperty
    private String skype;

    @ApiModelProperty
    private String urgentContact;

    @ApiModelProperty
    private String urgentPhoneNumber;

    @ApiModelProperty
    private String village;
}
