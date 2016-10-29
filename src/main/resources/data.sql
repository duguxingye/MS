CREATE SCHEMA `ms` ;

CREATE TABLE `ms`.`app_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `job_id` VARCHAR(30) BINARY NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `name` VARCHAR(30) NOT NULL,
  `phone` VARCHAR(30) NOT NULL,
  `leader_id` VARCHAR(30) NOT NULL,
  `has_locked` TINYINT(1) NOT NULL DEFAULT '0',
  `has_passed` TINYINT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `job_id_UNIQUE` (`job_id` ASC));

CREATE TABLE `ms`.`user_profile` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `type_UNIQUE` (`type` ASC));

CREATE TABLE `ms`.`app_user_user_profile` (
  `user_id` INT NOT NULL,
  `user_profile_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `user_profile_id`),
  INDEX `user_profile_id_idx` (`user_profile_id` ASC),
  CONSTRAINT `user_id`
  FOREIGN KEY (`user_id`)
  REFERENCES `ms`.`app_user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `user_profile_id`
  FOREIGN KEY (`user_profile_id`)
  REFERENCES `ms`.`user_profile` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `ms`.`persistent_login` (
  `username` VARCHAR(30) NOT NULL,
  `series` VARCHAR(64) NOT NULL,
  `token` VARCHAR(64) NOT NULL,
  `last_used` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`));

CREATE TABLE `ms`.`user_attempt` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(30) BINARY NOT NULL,
  `attempt` VARCHAR(30) NOT NULL,
  `last_modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC));

INSERT INTO `ms`.`user_profile` (`id`, `type`) VALUES ('1', 'ADMIN');
INSERT INTO `ms`.`user_profile` (`id`, `type`) VALUES ('2', 'AREA');
INSERT INTO `ms`.`user_profile` (`id`, `type`) VALUES ('3', 'GROUP');
INSERT INTO `ms`.`user_profile` (`id`, `type`) VALUES ('4', 'REGULAR');

-- password abc123456
INSERT INTO `ms`.`app_user` (`id`, `job_id`, `password`, `name`, `phone`, `leader_id`)
VALUES ('1', 'admin01', '$2a$10$TANjWG0UHIbBXpo204W4OO.Mv14znYi90DbLlgmXishiVsEpUrIJO', '管理员01', '10086', 'NONE');

INSERT INTO `ms`.`app_user` (`id`, `job_id`, `password`, `name`, `phone`, `leader_id`)
VALUES ('2', 'admin02', '$2a$10$TANjWG0UHIbBXpo204W4OO.Mv14znYi90DbLlgmXishiVsEpUrIJO', '管理员02', '10086', 'NONE');

INSERT INTO `ms`.`app_user` (`id`, `job_id`, `password`, `name`, `phone`, `leader_id`)
VALUES ('3', 'admin03', '$2a$10$TANjWG0UHIbBXpo204W4OO.Mv14znYi90DbLlgmXishiVsEpUrIJO', '管理员03', '10086', 'NONE');

SELECT id FROM  `ms`.`app_user` where job_id='admin01' ;
SELECT id FROM  `ms`.`user_profile` where type='ADMIN' ;

INSERT INTO `ms`.`app_user_user_profile` (`user_id`, `user_profile_id`) VALUES ('1', '1');
INSERT INTO `ms`.`app_user_user_profile` (`user_id`, `user_profile_id`) VALUES ('2', '1');
INSERT INTO `ms`.`app_user_user_profile` (`user_id`, `user_profile_id`) VALUES ('3', '1');

CREATE TABLE `ms`.`product_ins` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `company` VARCHAR(30) NOT NULL,
  `employee` VARCHAR(30) NOT NULL,
  `employee_id` VARCHAR(30) BINARY NOT NULL,
  `ins_company` VARCHAR(30) NOT NULL,
  `ins_type` VARCHAR(30) NOT NULL,
  `ins_illustration` VARCHAR(30) NOT NULL,
  `product_type` VARCHAR(30) NOT NULL,
  `ins_person` VARCHAR(30) NULL,
  `car_number` VARCHAR(30) NULL,
  `ins_time` VARCHAR(30) NOT NULL,
  `car_type` VARCHAR(30) NULL,
  `car_business_money` DECIMAL(10,2) NULL,
  `car_mandatory_money` DECIMAL(10,2) NULL,
  `car_tax_money` DECIMAL(10,2) NULL,
  `ins_money` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `employee_id`
  FOREIGN KEY (employee_id)
  REFERENCES `ms`.`app_user`(job_id)
    ON DELETE  CASCADE
    ON UPDATE CASCADE);

select distinct c.job_id
from ms.app_user a inner join ms.app_user b on a.job_id=b.leader_id or a.job_id=b.job_id
  inner join ms.app_user c on b.job_id=c.leader_id or b.job_id=c.job_id
where a.job_id='area01';

SELECT
  u.job_id
FROM
  ms.app_user AS u
  JOIN ms.app_user_user_profile AS up
    ON (u.id = up.user_id)
WHERE
  up.user_profile_id = '1';

SELECT
  u.job_id
FROM
  ms.app_user AS u
  JOIN ms.app_user_user_profile AS up
    ON (u.id = up.user_id)
  JOIN ms.user_profile AS p
    ON (up.user_profile_id = p.id)
WHERE
  p.type != 'ADMIN';

# alter table ms.product_ins change ins_illstration ins_illustration varchar(30);
# alter table ms.product_ins change ins_persion ins_person varchar(30);
# alter table ms.product_ins add column product_type varchar(30) not null;
alter table ms.app_user drop column has_passed;
