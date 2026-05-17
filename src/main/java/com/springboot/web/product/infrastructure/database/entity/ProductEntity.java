package com.springboot.web.product.infrastructure.database.entity;

import com.springboot.web.category.infrastructure.database.entity.CategoryEntity;
import com.springboot.web.productdetail.infrastructure.database.entity.ProductDetailEntity;
import com.springboot.web.review.infrastructure.database.entity.ReviewEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase ProductEntity que representa la entidad de products en la base de datos.
 * Se utiliza para mapear los datos de la base de datos a objetos Java.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_detail_id")
    private ProductDetailEntity productDetail;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewEntity> reviews = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    private List<CategoryEntity> categories = new ArrayList<>();

}
