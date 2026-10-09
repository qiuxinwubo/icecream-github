-- V1__init.sql
-- 冰淇淋管理系统 - 数据库初始化

-- 1. 厂商表
CREATE TABLE IF NOT EXISTS factory (
                                       id          INTEGER PRIMARY KEY AUTOINCREMENT,
                                       name        VARCHAR(100) NOT NULL UNIQUE,
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- 2. 商品表
CREATE TABLE IF NOT EXISTS goods (
                                     id               INTEGER PRIMARY KEY AUTOINCREMENT,
                                     barcode          VARCHAR(50)  NOT NULL UNIQUE,
    name             VARCHAR(200) NOT NULL,
    wholesale_price  DECIMAL(10,2) DEFAULT 0.00,
    retail_price     DECIMAL(10,2) DEFAULT 0.00,
    factory          VARCHAR(100),
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- 3. 初始厂商数据
INSERT OR IGNORE INTO factory (name) VALUES ('其他');
INSERT OR IGNORE INTO factory (name) VALUES ('伊利');
INSERT OR IGNORE INTO factory (name) VALUES ('东北大板');
INSERT OR IGNORE INTO factory (name) VALUES ('中街');
INSERT OR IGNORE INTO factory (name) VALUES ('未分类');
INSERT OR IGNORE INTO factory (name) VALUES ('四毛产品');
INSERT OR IGNORE INTO factory (name) VALUES ('八毛产品');
INSERT OR IGNORE INTO factory (name) VALUES ('和路雪');
INSERT OR IGNORE INTO factory (name) VALUES ('雀巢');
INSERT OR IGNORE INTO factory (name) VALUES ('顶喜');
INSERT OR IGNORE INTO factory (name) VALUES ('冻痴');
INSERT OR IGNORE INTO factory (name) VALUES ('德芙');
INSERT OR IGNORE INTO factory (name) VALUES ('蒙牛');
INSERT OR IGNORE INTO factory (name) VALUES ('茅台冰淇淋');
INSERT OR IGNORE INTO factory (name) VALUES ('旺旺碎冰冰');
INSERT OR IGNORE INTO factory (name) VALUES ('日本明治');
INSERT OR IGNORE INTO factory (name) VALUES ('优雪派');
INSERT OR IGNORE INTO factory (name) VALUES ('美登高');
INSERT OR IGNORE INTO factory (name) VALUES ('可米酷无蔗糖');
INSERT OR IGNORE INTO factory (name) VALUES ('好阿婆');
INSERT OR IGNORE INTO factory (name) VALUES ('九头牛');

-- 4. 示例商品数据
INSERT OR IGNORE INTO goods (barcode, name, wholesale_price, retail_price, factory)
VALUES ('6901234567890', '可爱多甜筒', 3.50, 5.00, '和路雪');

INSERT OR IGNORE INTO goods (barcode, name, wholesale_price, retail_price, factory)
VALUES ('6901234567891', '梦龙经典', 6.00, 9.00, '和路雪');

INSERT OR IGNORE INTO goods (barcode, name, wholesale_price, retail_price, factory)
VALUES ('6901234567892', '巧乐兹', 2.80, 4.00, '伊利');