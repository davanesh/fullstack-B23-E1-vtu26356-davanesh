INSERT IGNORE INTO customers (name, email) VALUES 
('Alice Johnson', 'alice@johnson.com'),
('Bob Smith', 'bob@smith.com'),
('Charlie Brown', 'charlie@brown.com');

INSERT IGNORE INTO products (name, price) VALUES 
('Laptop', 1200.00),
('Smartphone', 800.00),
('Headphones', 150.00),
('Monitor', 300.00);

INSERT IGNORE INTO orders (customer_id, product_id, amount, order_date) VALUES 
(1, 1, 1200.00, '2023-10-01'),
(1, 3, 150.00, '2023-10-05'),
(2, 2, 800.00, '2023-10-02'),
(2, 4, 300.00, '2023-10-10'),
(2, 1, 1200.00, '2023-10-15'),
(3, 3, 150.00, '2023-10-20');