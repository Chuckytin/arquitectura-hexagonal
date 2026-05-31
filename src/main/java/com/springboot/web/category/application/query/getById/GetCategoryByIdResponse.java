package com.springboot.web.category.application.query.getById;

import com.springboot.web.category.domain.entity.Category;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCategoryByIdResponse {

    private Category category;
}