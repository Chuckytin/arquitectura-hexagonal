package com.springboot.web.productdetail.infrastructure.api.mapper;

import com.springboot.web.productdetail.application.command.create.CreateProductDetailRequest;
import com.springboot.web.productdetail.application.command.update.UpdateProductDetailRequest;
import com.springboot.web.productdetail.domain.entity.ProductDetail;
import com.springboot.web.productdetail.infrastructure.api.dto.CreateProductDetailDto;
import com.springboot.web.productdetail.infrastructure.api.dto.ProductDetailDto;
import com.springboot.web.productdetail.infrastructure.api.dto.UpdateProductDetailDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface ProductDetailMapper {

    CreateProductDetailRequest mapToCreateProductDetailRequest(CreateProductDetailDto dto);

    UpdateProductDetailRequest mapToUpdateProductDetailRequest(UpdateProductDetailDto dto);

    @BeanMapping(ignoreUnmappedSourceProperties = {"product"})
    ProductDetailDto mapToProductDetailDto(ProductDetail productDetail);
}