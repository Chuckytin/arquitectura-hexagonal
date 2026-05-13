package com.springboot.web.product.domain.port;

import com.springboot.web.common.domain.PaginationQuery;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.entity.ProductFilter;

import java.util.Optional;

/**
 * Interfaz ProductRepository que define las operaciones de persistencia para la entidad Product.
 * Esta interfaz actúa como un contrato para cualquier implementación de repositorio que se encargue de manejar la persistencia de los productos,
 * permitiendo así una separación clara entre la lógica de negocio y la capa de acceso a datos.
 */
public interface ProductRepository {

    Product upsert(Product product);

    Optional<Product> findById(Long id);

    boolean existsById(Long id);

    PaginationResult<Product> findAll(PaginationQuery paginationQuery, ProductFilter productFilter);

    void deleteById(Long id);


}
