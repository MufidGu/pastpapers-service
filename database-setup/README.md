# Database Setup Guide

This directory contains scripts for setting up the PostgreSQL database with separate DDL and DML users for the Pastpapers Service.

## Security Architecture

The database uses two separate user roles to implement the principle of least privilege:

### 1. DDL User (`pastpapers_ddl_user`)
- **Purpose**: Schema management and migrations
- **Permissions**: Full DDL privileges (CREATE, ALTER, DROP, etc.)
- **Usage**:
  - CI/CD pipelines for automated migrations
  - Local development environment
  - Manual database schema changes
- **Security**: Should NOT be used by the running application

### 2. DML User (`pastpapers_dml_user`)
- **Purpose**: Runtime data operations
- **Permissions**: Limited to DML operations (SELECT, INSERT, UPDATE, DELETE)
- **Usage**: Application runtime only
- **Security**: Even if the application is compromised, attackers cannot modify the database schema

## Setup Instructions

### Prerequisites
- PostgreSQL 12 or higher installed
- Superuser access to PostgreSQL (e.g., `postgres` user)
- `psql` command-line tool available

### Step 1: Create Database and Users

Run the first script as a PostgreSQL superuser:

```bash
psql -U postgres -f 01-create-database-and-users.sql
```

This script will:
- Create the `pastpapers_db` database
- Create `pastpapers_ddl_user` with schema management privileges
- Create `pastpapers_dml_user` with limited data access privileges

### Step 2: Run Flyway Migrations

Use the DDL user to run database migrations:

```bash
# Using Gradle
./gradlew flywayMigrate \
  -Dflyway.url=jdbc:postgresql://localhost:5432/pastpapers_db \
  -Dflyway.user=pastpapers_ddl_user \
  -Dflyway.password=ddl_password

# Or let Spring Boot run migrations on startup
# Configure environment variables:
export DB_URL=jdbc:postgresql://localhost:5432/pastpapers_db
export DB_DDL_USERNAME=pastpapers_ddl_user
export DB_DDL_PASSWORD=ddl_password
```

### Step 3: Grant DML Permissions

After migrations are complete, grant permissions to the DML user:

```bash
psql -U postgres -d pastpapers_db -f 02-grant-dml-permissions.sql
```

### Step 4: Configure Application

Set environment variables for the application to use the DML user:

```bash
# Runtime database connection (DML user)
export DB_URL=jdbc:postgresql://localhost:5432/pastpapers_db
export DB_DML_USERNAME=pastpapers_dml_user
export DB_DML_PASSWORD=dml_password

# Migration database connection (DDL user)
export DB_DDL_USERNAME=pastpapers_ddl_user
export DB_DDL_PASSWORD=ddl_password
```

## Production Deployment Checklist

- [ ] Change default passwords in the SQL scripts before running
- [ ] Use strong, randomly generated passwords for both users
- [ ] Store passwords in a secure secrets management system (e.g., AWS Secrets Manager, HashiCorp Vault)
- [ ] Configure SSL/TLS for database connections
- [ ] Restrict database network access using firewall rules
- [ ] Use IAM database authentication if deploying on AWS RDS
- [ ] Configure automated backups
- [ ] Set up monitoring and alerting for database health

## Password Management

**CRITICAL**: The default passwords (`ddl_password` and `dml_password`) are placeholders and MUST be changed before deploying to any non-local environment.

### Changing Passwords

```sql
-- Connect as superuser
psql -U postgres -d pastpapers_db

-- Change DDL user password
ALTER USER pastpapers_ddl_user WITH PASSWORD 'your_strong_ddl_password';

-- Change DML user password
ALTER USER pastpapers_dml_user WITH PASSWORD 'your_strong_dml_password';
```

## Verifying Setup

### Check User Permissions

```sql
-- Connect to database
psql -U postgres -d pastpapers_db

-- List all users and their roles
\du

-- Check DML user permissions
SELECT grantee, table_name, privilege_type
FROM information_schema.table_privileges
WHERE grantee = 'pastpapers_dml_user'
ORDER BY table_name;
```

### Test DML User Restrictions

```sql
-- Try to connect as DML user
psql -U pastpapers_dml_user -d pastpapers_db

-- This should work (SELECT)
SELECT COUNT(*) FROM institutions;

-- This should FAIL (DDL operation)
CREATE TABLE test_table (id INT);
-- Expected: ERROR: permission denied for schema public

-- This verifies the DML user cannot modify schema
```

## Troubleshooting

### Issue: DML user cannot access tables

**Solution**: Run `02-grant-dml-permissions.sql` script to grant table-level permissions.

### Issue: Migrations fail with permission denied

**Solution**: Ensure you're running migrations with the DDL user credentials.

### Issue: Application cannot connect

**Solution**:
1. Verify credentials in environment variables
2. Check PostgreSQL `pg_hba.conf` for connection rules
3. Ensure PostgreSQL is accepting connections on the expected port (default: 5432)

## Local Development

For local development, you can use Docker Compose to run PostgreSQL:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: pastpapers_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./database-setup:/docker-entrypoint-initdb.d

volumes:
  postgres_data:
```

## References

- [PostgreSQL User Management](https://www.postgresql.org/docs/current/user-manag.html)
- [PostgreSQL Privileges](https://www.postgresql.org/docs/current/ddl-priv.html)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Boot Flyway Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
