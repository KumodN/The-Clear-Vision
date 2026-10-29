-- Create doctors table
CREATE TABLE doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(200),
    qualifications TEXT,
    bio TEXT,
    photo_url VARCHAR(500),
    email VARCHAR(255),
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    branch_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE SET NULL
);

-- Add doctor_id to appointments table
ALTER TABLE appointments ADD COLUMN doctor_id BIGINT;
ALTER TABLE appointments ADD FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE SET NULL;

-- Insert sample doctors
INSERT INTO doctors (first_name, last_name, specialization, qualifications, bio, email, phone, is_active, branch_id)
VALUES 
('Sarah', 'Johnson', 'Optometrist', 'OD, FAAO', 'Dr. Sarah Johnson specializes in comprehensive eye examinations and contact lens fittings.', 'sarah.johnson@theclearvision.com', '555-0201', TRUE, 1),
('Michael', 'Chen', 'Ophthalmologist', 'MD, FACS', 'Dr. Michael Chen is an expert in cataract surgery and refractive procedures.', 'michael.chen@theclearvision.com', '555-0202', TRUE, 1);
