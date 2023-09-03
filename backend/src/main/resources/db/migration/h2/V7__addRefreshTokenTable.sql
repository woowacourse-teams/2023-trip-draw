CREATE TABLE `refresh_token`
(
    `refresh_token_id` BIGINT,
    `member_id`        BIGINT,
    `token`            TEXT,
    `created_at`       TIMESTAMP,
    `updated_at`       TIMESTAMP
);

ALTER TABLE `refresh_token`
    ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);
