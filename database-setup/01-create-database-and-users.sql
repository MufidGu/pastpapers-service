-- Database Setup Script for Pastpapers Service
-- This script creates the database and two user roles: DDL and DML
--
-- Run this script as a PostgreSQL superuser (e.g., postgres user)
-- Example: psql -U postgres -f 01-create-database-and-users.sql

-- Create the database
CREATE DATABASE pastpapers_db
    WITH
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TEMPLATE = template0;

-- Connect to the newly created database
\c pastpapers_db

-- Create DDL User (for schema migrations and DDL operations)
-- This user has full privileges for schema management
CREATE USER pastpapers_ddl_user WITH PASSWORD 'ddl_password';

-- Grant schema creation and usage privileges to DDL user
GRANT CREATE ON DATABASE pastpapers_db TO pastpapers_ddl_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO pastpapers_ddl_user;

-- Allow DDL user to create and modify objects in the public schema
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL PRIVILEGES ON TABLES TO pastpapers_ddl_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL PRIVILEGES ON SEQUENCES TO pastpapers_ddl_user;

-- Create DML User (for runtime data operations only)
-- This user can only perform INSERT, UPDATE, DELETE, SELECT operations
CREATE USER pastpapers_dml_user WITH PASSWORD 'dml_password';

-- Grant CONNECT privilege to DML user
GRANT CONNECT ON DATABASE pastpapers_db TO pastpapers_dml_user;
GRANT USAGE ON SCHEMA public TO pastpapers_dml_user;

-- Note: Table-level permissions will be granted after tables are created
-- See 02-grant-dml-permissions.sql

-- Display user roles
\du

-- Summary
\echo 'Database setup completed:'
\echo '  - Database: pastpapers_db'
\echo '  - DDL User: pastpapers_ddl_user (for migrations, CI/CD, local development)'
\echo '  - DML User: pastpapers_dml_user (for runtime application use)'
\echo ''
\echo 'IMPORTANT: Change the default passwords before deploying to production!'
\echo 'IMPORTANT: Run migrations using pastpapers_ddl_user'
\echo 'IMPORTANT: Configure application to use pastpapers_dml_user for runtime'
