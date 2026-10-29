-- Insert sample products
INSERT INTO products (id, sku, title, slug, description, category_id, gender, material, base_price) VALUES
                                                                                                        (UUID(), 'EG-001', 'Classic Black Frames', 'classic-black-frames', 'Timeless classic black eyeglass frames for everyday wear', 1, 'UNISEX', 'Acetate', 129.99),
                                                                                                        (UUID(), 'SG-001', 'Aviator Sunglasses', 'aviator-sunglasses', 'Classic aviator style sunglasses with UV protection', 2, 'UNISEX', 'Metal', 89.99),
                                                                                                        (UUID(), 'RG-001', 'Reading Glasses +1.5', 'reading-glasses-1-5', 'Ready-made reading glasses for mild presbyopia', 4, 'UNISEX', 'Plastic', 29.99);

-- Insert sample variants
INSERT INTO product_variants (id, product_id, variant_name, frame_color, frame_size, frame_material, price, stock_quantity, sku_suffix, is_default)
SELECT
    UUID(),
    id,
    'Black - 52mm',
    'Black',
    '52mm',
    'Acetate',
    129.99,
    25,
    'BLK-52',
    TRUE
FROM products WHERE sku = 'EG-001';

INSERT INTO product_variants (id, product_id, variant_name, frame_color, frame_size, frame_material, price, stock_quantity, sku_suffix, is_default)
SELECT
    UUID(),
    id,
    'Gold - 55mm',
    'Gold',
    '55mm',
    'Metal',
    89.99,
    15,
    'GLD-55',
    TRUE
FROM products WHERE sku = 'SG-001';

-- Insert sample images
INSERT INTO product_images (product_variant_id, product_id, image_url, alt_text, display_order, is_primary)
SELECT
    pv.id,
    pv.product_id,
    'https://example.com/images/classic-black-52mm.jpg',
    'Classic Black Frames - 52mm',
    0,
    TRUE
FROM product_variants pv
         JOIN products p ON pv.product_id = p.id
WHERE p.sku = 'EG-001' AND pv.sku_suffix = 'BLK-52';