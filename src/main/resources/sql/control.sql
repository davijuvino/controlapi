
DROP TABLE users IF EXISTS;
CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `password` VARCHAR(255) DEFAULT NULL,
  `update_at` DATETIME DEFAULT NULL,
  `create_at` DATETIME NOT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE expenses IF EXISTS ;
CREATE TABLE `expenses` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) DEFAULT NULL,
  `category` VARCHAR(255) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `create_at` DATETIME NOT NULL,
  `update_at` DATETIME DEFAULT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE product IF EXISTS ;
CREATE TABLE `product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `amount` INT DEFAULT NULL,
  `price` DECIMAL(38,2) DEFAULT NULL,
  `date_at` DATE DEFAULT NULL,
  `create_at` DATETIME NOT NULL,
  `update_at` DATETIME DEFAULT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  `expense_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE `product`
  ADD CONSTRAINT `product_ibfk_1` FOREIGN KEY (`expense_id`) REFERENCES `expenses` (`id`);

ALTER TABLE `expenses`
  ADD CONSTRAINT `expenses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);