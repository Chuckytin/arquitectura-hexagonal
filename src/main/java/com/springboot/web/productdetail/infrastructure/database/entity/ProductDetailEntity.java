package com.springboot.web.productdetail.infrastructure.database.entity;

import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Clase ProductDetailEntity que representa la entidad de product_details en la base de datos.
 * Se utiliza para mapear los datos de la base de datos a objetos Java.
 */
@Entity
@Table(name = "product_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specifications;

    private String warranty;

    private String provider;

    @OneToOne(mappedBy = "productDetail")
    private ProductEntity product;

}
