package com.springboot.web.category.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {

    @NotNull(message = "Id is required")
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
}