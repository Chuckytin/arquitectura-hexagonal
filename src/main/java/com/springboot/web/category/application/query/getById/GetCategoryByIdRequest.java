package com.springboot.web.category.application.query.getById;

import com.springboot.web.common.application.mediator.Request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCategoryByIdRequest implements Request<GetCategoryByIdResponse> {

    private Long id;
}