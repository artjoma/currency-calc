CREATE TABLE IF NOT EXISTS currency_pairs
(
     id          SERIAL8         PRIMARY KEY,
     base        TEXT            NOT NULL,
     quote       TEXT            NOT NULL,
     fee_id      INT8            NULL,
     rate_id    INT8            NULL,
     pair_name   TEXT            NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS PAIR_NAME_INDEX ON currency_pairs (pair_name);