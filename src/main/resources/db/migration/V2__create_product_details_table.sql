CREATE TABLE IF NOT EXISTS product_details (
                                               id BIGSERIAL PRIMARY KEY,
                                               specifications TEXT,
                                               warranty VARCHAR(255),
    provider VARCHAR(255)
    );