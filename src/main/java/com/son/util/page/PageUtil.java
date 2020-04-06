package com.son.util.page;

import com.son.model.SortDirection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {

    public static Pageable getPageableByOffset(Integer offset, Integer limit, String sortDirection, String sortBy) {
        Sort.Direction direction = sortDirection.equals(SortDirection.ASC.toString()) ?
            Sort.Direction.ASC : Sort.Direction.DESC;

        return new OffsetBasedPageRequest(offset, limit, Sort.by(direction, sortBy));
    }

    public static Pageable getPageable(Integer currentPage, Integer limit, String sortDirection, String sortBy) {
        Sort.Direction direction = sortDirection.equals(SortDirection.ASC.toString()) ?
            Sort.Direction.ASC : Sort.Direction.DESC;

        return PageRequest.of(currentPage - 1, limit, Sort.by(direction, sortBy));
    }
}
