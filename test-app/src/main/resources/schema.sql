CREATE TABLE PurchaseTransaction (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(50) NOT NULL,
    transaction_date DATE NOT NULL,
    purchase_amount DECIMAL(10, 2) CHECK (purchase_amount >= 0) NOT NULL
);