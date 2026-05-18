CREATE TABLE IF NOT EXISTS reviews (
                                       id BIGSERIAL PRIMARY KEY,
                                       comment TEXT,
                                       score INTEGER,
                                       product_id BIGINT NOT NULL,
                                       CONSTRAINT fk_review_product
                                       FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE
    );