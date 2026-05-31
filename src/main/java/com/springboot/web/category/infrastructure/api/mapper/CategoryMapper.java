package com.springboot.web.category.infrastructure.api.mapper;

import com.springboot.web.category.application.command.create.CreateCategoryRequest;
import com.springboot.web.category.application.command.update.UpdateCategoryRequest;
import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.category.infrastructure.api.dto.CategoryDto;
import com.springboot.web.category.infrastructure.api.dto.CreateCategoryDto;
import com.springboot.web.category.infrastructure.api.dto.UpdateCategoryDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface CategoryMapper {

    CreateCategoryRequest mapToCreateCategoryRequest(CreateCategoryDto dto);

    UpdateCategoryRequest mapToUpdateCategoryRequest(UpdateCategoryDto dto);

    @BeanMapping(ignoreUnmappedSourceProperties = {"products"})
    CategoryDto mapToCategoryDto(Category category);
}