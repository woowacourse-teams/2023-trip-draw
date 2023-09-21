ALTER TABLE point
ADD year INT AS (year(recorded_at)) VIRTUAL,
ADD month INT AS (month(recorded_at)) VIRTUAL,
ADD day_of_week INT AS (DayOfWeek(recorded_at)) VIRTUAL,
ADD hour INT AS (hour(recorded_at)) VIRTUAL;

ALTER TABLE point
ADD INDEX ix_year (year),
ADD INDEX ix_month (month),
ADD INDEX ix_day_of_week (day_of_week),
ADD INDEX ix_hour (hour);

ALTER TABLE post
ADD INDEX ix_address (address);
