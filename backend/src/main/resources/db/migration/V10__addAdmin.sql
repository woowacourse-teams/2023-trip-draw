CREATE TABLE `Admin`
(
    `created_at` TIMESTAMP,
    `admin_id`   BIGINT PRIMARY KEY AUTO_INCREMENT,
    `updated_at` TIMESTAMP,
    `email`      VARCHAR(255),
    `password`   VARCHAR(255),
    `failCount`  BIGINT,
);
