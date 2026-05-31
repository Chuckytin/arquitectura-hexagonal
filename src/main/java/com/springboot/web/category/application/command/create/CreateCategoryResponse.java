package com.springboot.web.category.application.command.create;

import com.springboot.web.category.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryResponse {

    private Category category;
}