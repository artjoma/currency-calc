CREATE TABLE IF NOT EXISTS fees
(
     id          SERIAL8         PRIMARY KEY,
     fee         NUMERIC(24,6)   NOT NULL,
     updated_at  TIMESTAMP(3)    DEFAULT (now() AT TIME ZONE 'UTC') NOT NULL,
     pair_id     INT8            NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS PAIR_ID_INDEX ON fees (pair_id);