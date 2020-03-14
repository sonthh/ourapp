package com.son.util.page;

import com.son.entity.SortDirection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {
    public static Pageable getPageable(Integer offset, Integer limit, String sortDirection, String sortBy) {
        Sort.Direction direction = sortDirection.equals(SortDirection.ASC.toString()) ?
            Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = new OffsetBasedPageRequest(offset, limit, Sort.by(direction, sortBy));

        return pageable;
    }
}
