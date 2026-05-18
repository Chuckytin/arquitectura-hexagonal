CREATE TABLE IF NOT EXISTS products (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image VARCHAR(255),
    product_detail_id BIGINT,
    CONSTRAINT fk_product_detail
    FOREIGN KEY (product_detail_id)
    REFERENCES product_details(id)
    ON DELETE SET NULL
    );