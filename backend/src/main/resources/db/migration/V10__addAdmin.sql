CREATE TABLE `admin`
(
    `created_at` TIMESTAMP,
    `admin_id`   BIGINT PRIMARY KEY AUTO_INCREMENT,
    `updated_at` TIMESTAMP,
    `email`      VARCHAR(255),
    `password`   VARCHAR(255),
    `fail_count` BIGINT
);

CREATE TABLE `admin_session`
(
    `created_at`       TIMESTAMP,
    `admin_session_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `updated_at`       TIMESTAMP,
    `uuid`             VARCHAR(255),
    `expired_date_time` TIMESTAMP
);
