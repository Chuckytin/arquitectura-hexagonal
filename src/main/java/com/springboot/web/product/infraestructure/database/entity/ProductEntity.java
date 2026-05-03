package com.springboot.web.product.infraestructure.database.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Clase ProductEntity que representa la entidad de producto en la base de datos.
 * Se utiliza para mapear los datos de la base de datos a objetos Java.
 */
@Entity
@Table(name = "products")
@Data
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;
    
    private String description;

    @Column(nullable = false)
    private Double price;

    private String image;

}
