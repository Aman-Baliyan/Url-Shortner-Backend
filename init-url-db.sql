CREATE TABLE IF NOT EXISTS url_mapping (
    id BIGINT NOT NULL AUTO_INCREMENT,
    long_key VARCHAR(2048) NOT NULL,
    short_key VARCHAR(255) NOT NULL,
    expire_at DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_long_key (long_key(255)),
    KEY idx_short_key (short_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS short_key_data (
    id BIGINT NOT NULL AUTO_INCREMENT,
    short_key VARCHAR(255) DEFAULT NULL,
    long_key VARCHAR(2048) DEFAULT NULL,
    one_time TINYINT(1) NOT NULL DEFAULT 0,
    expire_at DATETIME(6) DEFAULT NULL,
    created_at DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_short_key (short_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
