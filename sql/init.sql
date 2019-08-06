DROP SCHEMA IF EXISTS fantaleghe CASCADE;
DROP DATABASE IF EXISTS fantaleghe;
DROP USER IF EXISTS fantaleghe;
SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE datname = 'fantaleghe' AND pid <> pg_backend_pid();
CREATE USER fantaleghe WITH PASSWORD 'fantaleghe' SUPERUSER;
CREATE schema fantaleghe AUTHORIZATION fantaleghe;
CREATE DATABASE fantaleghe;