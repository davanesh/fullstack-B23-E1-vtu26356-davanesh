-- Table for normal users
CREATE TABLE IF NOT EXISTS user_accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    balance DECIMAL(10,2) NOT NULL
);

-- Table for merchants
CREATE TABLE IF NOT EXISTS merchant_accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    balance DECIMAL(10,2) NOT NULL
);

-- Insert sample data
INSERT INTO user_accounts (name, balance) VALUES ('Alice', 500.00)
ON DUPLICATE KEY UPDATE balance=500.00;

INSERT INTO merchant_accounts (name, balance) VALUES ('Bob Store', 1000.00)
ON DUPLICATE KEY UPDATE balance=1000.00;
