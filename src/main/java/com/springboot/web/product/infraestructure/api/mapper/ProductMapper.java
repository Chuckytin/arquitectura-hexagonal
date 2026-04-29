package com.springboot.web.product.infraestructure.api.mapper;

import com.springboot.web.product.application.command.create.CreateProductRequest;
import com.springboot.web.product.application.command.update.UpdateProductRequest;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.infraestructure.api.dto.CreateProductDto;
import com.springboot.web.product.infraestructure.api.dto.ProductDto;
import com.springboot.web.product.infraestructure.api.dto.UpdateProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

    CreateProductRequest mapToCreateProductRequest(CreateProductDto createProductDto);

    UpdateProductRequest mapToUpdateProductRequest(UpdateProductDto updateProductDto);

    ProductDto mapToProductDto(Product product);

}
