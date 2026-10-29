CREATE TABLE customers_addresses (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     user_id BIGINT NOT NULL,
                                     label VARCHAR(50) NOT NULL,
                                     line1 VARCHAR(255) NOT NULL,
                                     line2 VARCHAR(255),
                                     city VARCHAR(100) NOT NULL,
                                     postal_code VARCHAR(20) NOT NULL,
                                     is_default BOOLEAN DEFAULT FALSE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_addresses_user_id ON customers_addresses(user_id);