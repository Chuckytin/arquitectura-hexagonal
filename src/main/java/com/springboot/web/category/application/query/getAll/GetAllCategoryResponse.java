package com.springboot.web.category.application.query.getAll;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.common.domain.PaginationResult;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllCategoryResponse {

    private PaginationResult<Category> paginationResult;
}