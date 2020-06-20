package com.son.dto;

import com.son.entity.Personnel;
import com.son.entity.TimeKeeping;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TimeKeepingView {
    private Personnel personnel;
    private List<TimeKeeping> timeKeepingList;
}
