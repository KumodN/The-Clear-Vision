-- Add OAuth provider fields to users table
-- Check and add auth_provider column
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'theclearvision' 
    AND TABLE_NAME = 'users' 
    AND COLUMN_NAME = 'auth_provider');

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE users ADD COLUMN auth_provider VARCHAR(20) DEFAULT ''LOCAL''',
    'SELECT ''Column auth_provider already exists'' AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add provider_id column
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'theclearvision' 
    AND TABLE_NAME = 'users' 
    AND COLUMN_NAME = 'provider_id');

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE users ADD COLUMN provider_id VARCHAR(255)',
    'SELECT ''Column provider_id already exists'' AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Create index on provider_id for faster lookups (if not exists)
SET @index_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
    WHERE TABLE_SCHEMA = 'theclearvision' 
    AND TABLE_NAME = 'users' 
    AND INDEX_NAME = 'idx_users_provider_id');

SET @sql = IF(@index_exists = 0,
    'CREATE INDEX idx_users_provider_id ON users(provider_id)',
    'SELECT ''Index idx_users_provider_id already exists'' AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
