CREATE TABLE `refresh_token`
(
    `refresh_token_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `member_id`        BIGINT,
    `token`            TEXT,
    `created_at`       TIMESTAMP,
    `updated_at`       TIMESTAMP
);
