package com.springboot.web.review.infrastructure.database.entity;

import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private Integer score;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

}
