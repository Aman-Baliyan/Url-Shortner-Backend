CREATE TABLE IF NOT EXISTS user_data (
    user_id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) DEFAULT NULL,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
