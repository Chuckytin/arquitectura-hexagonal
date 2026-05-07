package com.springboot.web.common.domain;

public record PaginationQuery(
        int page,
        int size
) {
}
