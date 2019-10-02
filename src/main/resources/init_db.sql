CREATE SCHEMA `internet-shop` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `internet-shop`.`items` (
    `item_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
     `name` VARCHAR(255) NOT NULL,
     `price` DECIMAL(6,2) NOT NULL,
    PRIMARY KEY (`item_id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

INSERT INTO `internet-shop`.`items` (`name`, `price`) VALUES ('Vanvan', '4300');

ALTER TABLE `internet-shop`.`items`
    CHANGE COLUMN `price` `price` DECIMAL(8,2) NOT NULL ;

INSERT INTO `internet-shop`.`items` (`name`, `price`) VALUES ('Hayabusa', '14799');

INSERT INTO `internet-shop`.`items` (`name`, `price`) VALUES ('GSX-R1000R', '17699');
INSERT INTO `internet-shop`.`items` (`name`, `price`) VALUES ('GSX-R600', '11399');
INSERT INTO `internet-shop`.`items` (`name`, `price`) VALUES ('GSX250R', '4549');
INSERT INTO `internet-shop`.`items` (`name`, `price`) VALUES ('Bandit 1250', '9899');
INSERT INTO `internet-shop`.`items` (`name`, `price`) VALUES ('KATANA', '13499');
INSERT INTO `internet-shop`.`items` (`name`, `price`) VALUES ('V-Strom 1000', '12999');
INSERT INTO `internet-shop`.`items` (`name`, `price`) VALUES ('V-Strom 650XT', '9299');

CREATE TABLE `internet-shop`.`orders` (
  `order_id` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`order_id`));

  CREATE TABLE `internet-shop`.`orders_items` (
  `orders_items_id` INT NOT NULL AUTO_INCREMENT,
  `order_id` INT NOT NULL,
  `item_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`orders_items_id`),
  INDEX `orders_items_orders_fk_idx` (`order_id` ASC) VISIBLE,
  INDEX `orders_items_items_fk_idx` (`item_id` ASC) VISIBLE,
  CONSTRAINT `orders_items_orders_fk`
    FOREIGN KEY (`order_id`)
    REFERENCES `internet-shop`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `orders_items_items_fk`
    FOREIGN KEY (`item_id`)
    REFERENCES `internet-shop`.`items` (`item_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `internet-shop`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `token` VARCHAR(45) NULL,
  PRIMARY KEY (`user_id`));

ALTER TABLE `internet-shop`.`orders`
ADD COLUMN `user_id` INT NOT NULL AFTER `order_id`,
ADD INDEX `orders_users_fk_idx` (`user_id` ASC) VISIBLE;
;
ALTER TABLE `internet-shop`.`orders`
ADD CONSTRAINT `orders_users_fk`
  FOREIGN KEY (`user_id`)
  REFERENCES `internet-shop`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `internet-shop`.`roles` (
  `role_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`role_id`));

INSERT INTO `internet-shop`.`roles` (`name`) VALUES ('ADMIN');
INSERT INTO `internet-shop`.`roles` (`name`) VALUES ('USER');

CREATE TABLE `internet-shop`.`users_roles` (
  `users_roles_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`users_roles_id`),
  INDEX `users_roles_users_fk_idx` (`user_id` ASC) VISIBLE,
  INDEX `users_roles_roles_fk_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `users_roles_users_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `internet-shop`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `users_roles_roles_fk`
    FOREIGN KEY (`role_id`)
    REFERENCES `internet-shop`.`roles` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

INSERT INTO `internet-shop`.`users_roles` (`user_id`, `role_id`) VALUES ('1', '1');
INSERT INTO `internet-shop`.`users_roles` (`user_id`, `role_id`) VALUES ('2', '2');

CREATE TABLE `internet-shop`.`buckets` (
                                           `bucket_id` INT NOT NULL AUTO_INCREMENT,
                                           `user_id` INT NOT NULL,
                                           PRIMARY KEY (`bucket_id`),
                                           INDEX `buckets_users_fk_idx` (`user_id` ASC) VISIBLE,
                                           CONSTRAINT `buckets_users_fk`
                                               FOREIGN KEY (`user_id`)
                                                   REFERENCES `internet-shop`.`users` (`user_id`)
                                                   ON DELETE NO ACTION
                                                   ON UPDATE NO ACTION);

CREATE TABLE `internet-shop`.`buckets_items` (
                                                 `buckets_items_id` INT NOT NULL AUTO_INCREMENT,
                                                 `bucket_id` INT NOT NULL,
                                                 `item_id` INT NOT NULL,
                                                 PRIMARY KEY (`buckets_items_id`),
                                                 INDEX `buckets_items_items_fk_idx` (`item_id` ASC) VISIBLE,
                                                 INDEX `buckets_items_buckets_fk_idx` (`bucket_id` ASC) VISIBLE,
                                                 CONSTRAINT `buckets_items_items_fk`
                                                     FOREIGN KEY (`item_id`)
                                                         REFERENCES `internet-shop`.`items` (`item_id`)
                                                         ON DELETE NO ACTION
                                                         ON UPDATE NO ACTION,
                                                 CONSTRAINT `buckets_items_buckets_fk`
                                                     FOREIGN KEY (`bucket_id`)
                                                         REFERENCES `internet-shop`.`buckets` (`bucket_id`)
                                                         ON DELETE NO ACTION
                                                         ON UPDATE NO ACTION);

