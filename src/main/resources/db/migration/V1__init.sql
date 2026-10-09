-- V1__init.sql
CREATE TABLE IF NOT EXISTS factory (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       name VARCHAR(100) NOT NULL UNIQUE,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS goods (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     barcode VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    wholesale_price DECIMAL(10,2) DEFAULT 0,
    retail_price DECIMAL(10,2) DEFAULT 0,
    vendor_id INTEGER,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_id) REFERENCES factory(id)
    );

INSERT OR IGNORE INTO factory(id, name) VALUES(1, '测试冷饮厂');
INSERT OR IGNORE INTO goods(barcode, name, wholesale_price, retail_price, vendor_id)
VALUES('6901234567890', '测试雪糕', 3.50, 5.00, 1);