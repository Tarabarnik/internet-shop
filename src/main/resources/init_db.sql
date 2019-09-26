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
