package com.son.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountRequest {
    private Integer waiting;
    private Integer approved;
    private Integer rejected;
}
