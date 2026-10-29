-- Create product categories table
CREATE TABLE product_categories (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(100) NOT NULL UNIQUE,
                                    description TEXT,
                                    slug VARCHAR(100) NOT NULL UNIQUE,
                                    parent_id BIGINT NULL,
                                    is_active BOOLEAN DEFAULT TRUE,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    FOREIGN KEY (parent_id) REFERENCES product_categories(id) ON DELETE SET NULL
);

-- Create products table
CREATE TABLE products (
                          id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                          sku VARCHAR(100) UNIQUE NOT NULL,
                          title VARCHAR(255) NOT NULL,
                          slug VARCHAR(255) UNIQUE NOT NULL,
                          description TEXT,
                          category_id BIGINT NOT NULL,
                          gender ENUM('MEN', 'WOMEN', 'UNISEX', 'KIDS') DEFAULT 'UNISEX',
                          material VARCHAR(100),
                          base_price DECIMAL(10,2) NOT NULL,
                          is_active BOOLEAN DEFAULT TRUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (category_id) REFERENCES product_categories(id)
);

-- Create product_variants table
CREATE TABLE product_variants (
                                  id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
                                  product_id VARCHAR(36) NOT NULL,
                                  variant_name VARCHAR(255) NOT NULL,
                                  frame_color VARCHAR(50) NOT NULL,
                                  frame_size VARCHAR(20) NOT NULL,
                                  frame_material VARCHAR(100),
                                  price DECIMAL(10,2) NOT NULL,
                                  stock_quantity INT DEFAULT 0,
                                  sku_suffix VARCHAR(50) NOT NULL,
                                  is_default BOOLEAN DEFAULT FALSE,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Create product_images table
CREATE TABLE product_images (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                product_variant_id VARCHAR(36) NULL,
                                product_id VARCHAR(36) NOT NULL,
                                image_url VARCHAR(500) NOT NULL,
                                alt_text VARCHAR(255),
                                display_order INT DEFAULT 0,
                                is_primary BOOLEAN DEFAULT FALSE,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE CASCADE,
                                FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX idx_products_slug ON products(slug);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_active ON products(is_active);
CREATE INDEX idx_product_variants_product ON product_variants(product_id);
CREATE INDEX idx_product_variants_stock ON product_variants(stock_quantity);
CREATE INDEX idx_product_images_variant ON product_images(product_variant_id);
CREATE INDEX idx_product_images_product ON product_images(product_id);

-- Insert sample categories
INSERT INTO product_categories (name, description, slug) VALUES
                                                             ('Eyeglasses', 'Prescription eyeglasses for vision correction', 'eyeglasses'),
                                                             ('Sunglasses', 'Sunglasses for UV protection and style', 'sunglasses'),
                                                             ('Contact Lenses', 'Daily and monthly contact lenses', 'contact-lenses'),
                                                             ('Reading Glasses', 'Ready-made reading glasses', 'reading-glasses');