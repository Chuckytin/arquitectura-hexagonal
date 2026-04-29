package com.springboot.web.product.infraestructure.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDto {

    private Long id;

    @NotBlank
    private String name;

    @Length(min = 10, max = 255, message = "Description must be between 10 and 255 characters")
    private String description;

    @DecimalMin(value = "0.01", inclusive = false)
    @DecimalMax(value = "10000.00", inclusive = false)
    private Double price;

    private MultipartFile file;

}
