CREATE TABLE IF NOT EXISTS catalog_products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    price NUMERIC(12, 6) NOT NULL,
    currency VARCHAR(4) NOT NULL
);