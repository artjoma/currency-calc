CREATE TABLE IF NOT EXISTS rates
(
     id          SERIAL8         PRIMARY KEY,
     rate        NUMERIC(24, 6)  DEFAULT 0   NOT NULL,
     created_at  TIMESTAMP(3)    DEFAULT (now() AT TIME ZONE 'UTC') NOT NULL,
     pair_id     int8
);

CREATE UNIQUE INDEX IF NOT EXISTS FEE_PAIR_ID_INDEX ON rates (pair_id);