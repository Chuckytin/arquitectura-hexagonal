package com.springboot.web.product.infraestructure.database.entity;

import lombok.Data;

/**
 * Clase ProductEntity que representa la entidad de producto en la base de datos.
 */
@Data
public class ProductEntity {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String image;

}
