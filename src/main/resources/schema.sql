CREATE TABLE IF NOT EXISTS payments (
    idx bigint AUTO_INCREMENT PRIMARY KEY,
    user_idx bigint NOT NULL,
    amount int NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL
);