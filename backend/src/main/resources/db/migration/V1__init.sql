CREATE TABLE `member`
(
    `created_at` TIMESTAMP,
    `member_id`  BIGINT PRIMARY KEY AUTO_INCREMENT,
    `updated_at` TIMESTAMP,
    `nickname`   VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE `point`
(
    `has_post`    BOOLEAN   NOT NULL,
    `is_deleted`  BOOLEAN   NOT NULL,
    `latitude`    DOUBLE    NOT NULL,
    `longitude`   DOUBLE    NOT NULL,
    `created_at`  TIMESTAMP,
    `point_id`    BIGINT PRIMARY KEY AUTO_INCREMENT,
    `recorded_at` TIMESTAMP NOT NULL,
    `trip_id`     BIGINT    NOT NULL,
    `updated_at`  TIMESTAMP
);

CREATE TABLE `post`
(
    `created_at` TIMESTAMP,
    `member_id`  BIGINT,
    `point_id`   BIGINT UNIQUE,
    `post_id`    BIGINT PRIMARY KEY AUTO_INCREMENT,
    `trip_id`    BIGINT       NOT NULL,
    `updated_at` TIMESTAMP,
    `address`    VARCHAR(255) NOT NULL,
    `title`      VARCHAR(255) NOT NULL,
    `writing`    TEXT
);

CREATE TABLE `trip`
(
    `status`     TINYINT NOT NULL,
    `created_at` TIMESTAMP,
    `member_id`  BIGINT,
    `trip_id`    BIGINT PRIMARY KEY AUTO_INCREMENT,
    `updated_at` TIMESTAMP,
    `name`       VARCHAR(255)
);

ALTER TABLE `point`
    ADD FOREIGN KEY (`trip_id`) REFERENCES `trip` (`trip_id`);

ALTER TABLE `post`
    ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE `post`
    ADD FOREIGN KEY (`point_id`) REFERENCES `point` (`point_id`);

ALTER TABLE `trip`
    ADD FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);
