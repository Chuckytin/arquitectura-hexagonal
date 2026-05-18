CREATE TABLE IF NOT EXISTS products_categories (
                                                   product_id BIGINT NOT NULL,
                                                   category_id BIGINT NOT NULL,
                                                   PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product
    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE,
    CONSTRAINT fk_category
    FOREIGN KEY (category_id)
    REFERENCES categories(id)
    ON DELETE CASCADE
    );