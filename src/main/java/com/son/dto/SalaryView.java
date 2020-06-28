package com.son.dto;

import com.son.entity.Personnel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalaryView {
    private Personnel personnel;
    private Integer timeOn;
    private Integer timeOff;
    private Integer late;
    private Integer allowance;
    private Integer fare;
    private Integer advance;
    private Integer totalSalary;
}
