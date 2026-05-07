package com.springboot.web.common.domain;

import java.util.List;

public record PaginationResult<T>(
        List<T> content,
        int page,
        int size,
        int totalPages,
        long totalElements
) {
}
