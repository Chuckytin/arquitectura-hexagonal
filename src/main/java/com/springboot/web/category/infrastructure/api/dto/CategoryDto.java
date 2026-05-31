package com.springboot.web.category.infrastructure.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;
    private String name;
}