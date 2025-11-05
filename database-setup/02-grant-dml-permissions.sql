-- Grant DML Permissions Script
-- This script grants SELECT, INSERT, UPDATE, DELETE permissions to the DML user
-- Run this script AFTER running Flyway migrations as the DDL user
--
-- Example: psql -U postgres -d pastpapers_db -f 02-grant-dml-permissions.sql

-- Connect to the database
\c pastpapers_db

-- Grant SELECT, INSERT, UPDATE, DELETE on all existing tables in public schema
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO pastpapers_dml_user;

-- Grant USAGE and SELECT on all sequences (for ID generation if needed)
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO pastpapers_dml_user;

-- Configure default privileges for future tables created by DDL user
-- This ensures new tables automatically grant permissions to DML user
ALTER DEFAULT PRIVILEGES FOR ROLE pastpapers_ddl_user IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO pastpapers_dml_user;

ALTER DEFAULT PRIVILEGES FOR ROLE pastpapers_ddl_user IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO pastpapers_dml_user;

-- Verify permissions
\echo 'Checking permissions for pastpapers_dml_user...'
SELECT
    grantee,
    table_schema,
    table_name,
    privilege_type
FROM information_schema.table_privileges
WHERE grantee = 'pastpapers_dml_user'
    AND table_schema = 'public'
ORDER BY table_name, privilege_type;

\echo ''
\echo 'DML permissions granted successfully!'
\echo 'The pastpapers_dml_user can now perform SELECT, INSERT, UPDATE, DELETE operations.'
\echo 'The pastpapers_dml_user CANNOT perform DDL operations (CREATE, ALTER, DROP).'
