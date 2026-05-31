package com.springboot.web.category.application.command.assign;

import com.springboot.web.common.application.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignCategoryToProductRequest implements Request<Void> {

    private Long categoryId;
    private Long productId;
}