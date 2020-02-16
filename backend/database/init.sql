SELECT datname FROM pg_database;

-------------------- Create tables
DROP SCHEMA public cascade;
CREATE SCHEMA IF NOT EXISTS public;

\i fees_tbl.sql
\i rates_tbl.sql
\i currency_pairs_tbl.sql
