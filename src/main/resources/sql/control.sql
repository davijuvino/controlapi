
DROP TABLE users IF EXISTS;
CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) DEFAULT NULL,
  `email` VARCHAR(50) DEFAULT NULL,
  `login` VARCHAR(50) DEFAULT NULL,
  `password` VARCHAR(50) DEFAULT NULL,
  `update_at` DATETIME DEFAULT NULL,
  `create_at` DATETIME NOT NULL,
  `delete_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE expenses IF EXISTS ;
CREATE TABLE `expenses` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) DEFAULT NULL,
  `category` VARCHAR(100) DEFAULT NULL,
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

DROP TABLE npl_profile IF EXISTS ;
CREATE TABLE npl_profile (
    id BIGINT PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

DROP TABLE npl_resource_permission_profile IF EXISTS ;
CREATE TABLE npl_resource_permission_profile (
    id BIGINT PRIMARY KEY,
    profile_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL
);

DROP TABLE npl_user_profile IF EXISTS ;
CREATE TABLE npl_user_profile (
    id BIGINT PRIMARY KEY,
    profile_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

DROP TABLE npl_verifying_user IF EXISTS ;
CREATE TABLE npl_verifying_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    expiration_date TIMESTAMP(6) NOT NULL,
    uuid CHAR(36) NOT NULL,
    user_id BIGINT NOT NULL
);

DROP TABLE user_resource IF EXISTS ;
CREATE TABLE user_resource (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    key_id VARCHAR(255),
    name VARCHAR(255)
);

ALTER TABLE expenses
    ADD CONSTRAINT FK_expenses_users FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE npl_resource_permission_profile
    ADD CONSTRAINT FK_profile FOREIGN KEY (profile_id) REFERENCES npl_profile(id);

ALTER TABLE npl_resource_permission_profile
    ADD CONSTRAINT FK_resource FOREIGN KEY (resource_id) REFERENCES user_resource(id);

ALTER TABLE npl_user_profile
    ADD CONSTRAINT FK_user_profile FOREIGN KEY (profile_id) REFERENCES npl_profile(id);

ALTER TABLE npl_user_profile
    ADD CONSTRAINT FK_user FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE npl_verifying_user
    ADD CONSTRAINT FK_verifying_user FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE product
    ADD CONSTRAINT FK_product_expense FOREIGN KEY (expense_id) REFERENCES expenses(id);

