CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    gender ENUM('MALE', 'FEMALE') NOT NULL,
    birth DATE NOT NULL,
    refresh_token VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS member_role (
    member_role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    role ENUM('USER', 'ADMIN') NOT NULL,
    FOREIGN KEY (member_id) REFERENCES Member(id)
);