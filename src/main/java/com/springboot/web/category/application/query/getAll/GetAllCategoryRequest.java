package com.springboot.web.category.application.query.getAll;

import com.springboot.web.common.application.mediator.Request;
import com.springboot.web.common.domain.PaginationQuery;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllCategoryRequest implements Request<GetAllCategoryResponse> {

    private PaginationQuery paginationQuery;
}