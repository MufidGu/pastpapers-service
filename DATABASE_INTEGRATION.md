# Database Integration Documentation

This document describes the PostgreSQL database integration with Flyway migrations implemented for the Pastpapers Service.

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Database Schema](#database-schema)
4. [Security Model](#security-model)
5. [Audit Fields](#audit-fields)
6. [Setup Instructions](#setup-instructions)
7. [Configuration](#configuration)
8. [Migration Management](#migration-management)
9. [Testing](#testing)
10. [Troubleshooting](#troubleshooting)

## Overview

The pastpapers-service now uses PostgreSQL as its persistent database with the following features:

- **PostgreSQL 12+** as the RDBMS
- **Flyway** for database migrations
- **Spring Data JPA** with Hibernate for ORM
- **Audit fields** on all tables (createdAt, updatedAt, createdBy, updatedBy)
- **Two-user security model** (DDL user for migrations, DML user for runtime)
- **Cascading delete rules** for referential integrity

## Architecture

### Hexagonal Architecture

The application maintains clean hexagonal architecture:

```
Domain Layer (Records)
    ↓
SPI Interfaces (Ports)
    ↓
JPA Adapters (Infrastructure)
    ↓
JPA Repositories & Entities
    ↓
PostgreSQL Database
```

### Package Structure

```
src/main/java/com/mufidgu/pastpapers/
├── domain/                          # Domain models (records)
│   ├── course/
│   │   ├── Course.java             # Domain record
│   │   └── spi/Courses.java        # Port interface
│   ├── degree/
│   ├── institution/
│   ├── instructor/
│   ├── paper/
│   └── user/
└── infrastructure/
    ├── configuration/
    │   └── JpaAuditingConfiguration.java
    └── persistence/
        ├── entity/                  # JPA entities
        │   ├── AuditableEntity.java
        │   ├── CourseEntity.java
        │   ├── DegreeEntity.java
        │   ├── InstitutionEntity.java
        │   ├── InstructorEntity.java
        │   ├── PaperEntity.java
        │   └── UserEntity.java
        ├── repository/              # Spring Data JPA repositories
        │   ├── CourseJpaRepository.java
        │   ├── DegreeJpaRepository.java
        │   ├── InstitutionJpaRepository.java
        │   ├── InstructorJpaRepository.java
        │   ├── PaperJpaRepository.java
        │   └── UserJpaRepository.java
        └── adapter/                 # SPI implementations
            ├── CourseRepositoryAdapter.java
            ├── DegreeRepositoryAdapter.java
            ├── InstitutionRepositoryAdapter.java
            ├── InstructorRepositoryAdapter.java
            ├── PaperRepositoryAdapter.java
            └── UserRepositoryAdapter.java
```

## Database Schema

### Entity Relationships

```
institutions (base entity)
    ↓
    ├── degrees (many-to-many with institutions)
    │   └── users (many-to-one with degrees)
    │       └── papers (many-to-one with users, CASCADE DELETE)
    ├── courses (many-to-many with institutions and degrees)
    │   └── instructors (many-to-many with courses)
    └── papers (many-to-one with institution, SET NULL on delete)
```

### Tables

1. **institutions** - Educational institutions
   - Primary Key: `id` (UUID)
   - Unique: `short_name`

2. **degrees** - Academic degrees
   - Primary Key: `id` (UUID)
   - Unique: `short_name`
   - Many-to-many with institutions via `degree_institutions`

3. **courses** - Academic courses
   - Primary Key: `id` (UUID)
   - Unique: `short_name`
   - Many-to-many with degrees via `course_degrees`
   - Many-to-many with institutions via `course_institutions`

4. **instructors** - Faculty members
   - Primary Key: `id` (UUID)
   - Many-to-many with courses via `instructor_courses`
   - Many-to-many with institutions via `instructor_institutions`

5. **users** - Students (authenticated via Google OAuth)
   - Primary Key: `google_id` (VARCHAR)
   - Foreign Keys: `institution_id`, `degree_id` (nullable, SET NULL on delete)

6. **papers** - Past exam papers
   - Primary Key: `id` (UUID)
   - Foreign Key: `user_id` (required, CASCADE on delete)
   - Foreign Keys: `instructor_id`, `course_id`, `institution_id`, `degree_id` (nullable, SET NULL on delete)
   - Contains: `file_content` (BYTEA) for storing PDF/document data

### Cascading Rules

| Relationship | Delete Behavior | Rationale |
|-------------|-----------------|-----------|
| Paper → User | CASCADE | Papers belong to users; delete papers when user is deleted |
| User → Institution/Degree | SET NULL | Preserve user data even if institution/degree is deleted |
| Paper → Course/Instructor/Institution/Degree | SET NULL | Preserve paper metadata even if referenced entities are deleted |
| Junction Tables | CASCADE | Remove relationships when parent entity is deleted |

## Security Model

### Two-User Architecture

#### DDL User (`pastpapers_ddl_user`)

**Purpose**: Schema management and migrations

**Permissions**:
- Full DDL privileges (CREATE, ALTER, DROP)
- Full DML privileges (SELECT, INSERT, UPDATE, DELETE)
- Can create tables, indexes, constraints

**Usage**:
- Flyway migrations during application startup
- CI/CD pipeline for database schema updates
- Local development for schema changes

**Configuration**:
```properties
spring.flyway.user=${DB_DDL_USERNAME:pastpapers_ddl_user}
spring.flyway.password=${DB_DDL_PASSWORD:ddl_password}
```

#### DML User (`pastpapers_dml_user`)

**Purpose**: Runtime data operations only

**Permissions**:
- SELECT, INSERT, UPDATE, DELETE on all tables
- NO DDL permissions (cannot alter schema)

**Usage**:
- Application runtime only
- All database operations during normal application execution

**Configuration**:
```properties
spring.datasource.username=${DB_DML_USERNAME:pastpapers_dml_user}
spring.datasource.password=${DB_DML_PASSWORD:dml_password}
```

**Security Benefit**: Even if the application is compromised, attackers cannot modify the database schema because the runtime user has no DDL privileges.

## Audit Fields

All tables include four audit fields populated automatically via JPA Auditing:

| Field | Type | Description | Populated By |
|-------|------|-------------|--------------|
| `created_at` | TIMESTAMP | Record creation timestamp | JPA `@CreatedDate` |
| `updated_at` | TIMESTAMP | Last update timestamp | JPA `@LastModifiedDate` |
| `created_by` | VARCHAR(255) | User who created the record | JPA `@CreatedBy` |
| `updated_by` | VARCHAR(255) | User who last updated the record | JPA `@LastModifiedBy` |

### Auditor Resolution

The `JpaAuditingConfiguration` extracts the Google ID from the JWT token in the security context:

```java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // Extracts Google ID from JWT subject claim
        // Falls back to "system" for non-authenticated operations
    }
}
```

## Setup Instructions

### 1. Prerequisites

- PostgreSQL 12 or higher
- Java 21
- Gradle 8.14+

### 2. Database Setup

#### Option A: Automated Setup (Recommended)

```bash
cd database-setup
psql -U postgres -f 01-create-database-and-users.sql
```

This creates:
- Database: `pastpapers_db`
- DDL User: `pastpapers_ddl_user`
- DML User: `pastpapers_dml_user`

#### Option B: Manual Setup

```sql
-- Connect to PostgreSQL as superuser
psql -U postgres

-- Create database
CREATE DATABASE pastpapers_db;

-- Connect to the database
\c pastpapers_db

-- Create users
CREATE USER pastpapers_ddl_user WITH PASSWORD 'your_secure_ddl_password';
CREATE USER pastpapers_dml_user WITH PASSWORD 'your_secure_dml_password';

-- Grant privileges
GRANT CREATE ON DATABASE pastpapers_db TO pastpapers_ddl_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO pastpapers_ddl_user;
GRANT CONNECT ON DATABASE pastpapers_db TO pastpapers_dml_user;
GRANT USAGE ON SCHEMA public TO pastpapers_dml_user;
```

### 3. Run Migrations

Migrations run automatically on application startup via Flyway. Ensure DDL user credentials are configured:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/pastpapers_db
export DB_DDL_USERNAME=pastpapers_ddl_user
export DB_DDL_PASSWORD=your_secure_ddl_password
export DB_DML_USERNAME=pastpapers_dml_user
export DB_DML_PASSWORD=your_secure_dml_password

./gradlew bootRun
```

### 4. Grant DML Permissions

After migrations complete, grant permissions to the DML user:

```bash
psql -U postgres -d pastpapers_db -f database-setup/02-grant-dml-permissions.sql
```

## Configuration

### Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `DB_URL` | Yes | `jdbc:postgresql://localhost:5432/pastpapers_db` | Database JDBC URL |
| `DB_DDL_USERNAME` | Yes | `pastpapers_ddl_user` | DDL user for migrations |
| `DB_DDL_PASSWORD` | Yes | `ddl_password` | DDL user password |
| `DB_DML_USERNAME` | Yes | `pastpapers_dml_user` | DML user for runtime |
| `DB_DML_PASSWORD` | Yes | `dml_password` | DML user password |

### Application Properties

**Base Configuration** (`application.properties`):

```properties
# Runtime Database Connection (DML User)
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_DML_USERNAME}
spring.datasource.password=${DB_DML_PASSWORD}

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Flyway Configuration (DDL User)
spring.flyway.enabled=true
spring.flyway.url=${DB_URL}
spring.flyway.user=${DB_DDL_USERNAME}
spring.flyway.password=${DB_DDL_PASSWORD}
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

**Development Profile** (`application-dev.properties`):

```properties
spring.jpa.show-sql=true
spring.flyway.clean-disabled=false
```

**Production Profile** (`application-prod.properties`):

- Use environment variables for all credentials
- Enable SSL for database connections
- Disable SQL logging (`spring.jpa.show-sql=false`)
- Enable Flyway clean protection (`spring.flyway.clean-disabled=true`)

## Migration Management

### Migration File Naming

Flyway migrations follow the pattern: `V{version}__{description}.sql`

Example: `V1__initial_schema.sql`

### Current Migrations

1. **V1__initial_schema.sql**
   - Creates all tables with audit fields
   - Sets up foreign key constraints with cascading rules
   - Creates indexes for performance
   - Adds table comments

### Creating New Migrations

1. Create a new SQL file in `src/main/resources/db/migration/`
2. Follow naming convention: `V{next_version}__{description}.sql`
3. Write idempotent SQL (avoid duplicate operations)
4. Test locally before committing

Example:

```sql
-- V2__add_paper_status.sql

-- Add status column to papers table
ALTER TABLE papers
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDING';

-- Create index for status queries
CREATE INDEX idx_papers_status ON papers(status);
```

### Rolling Back Migrations

Flyway supports paid edition for automatic rollback. For free version:

1. Create a new migration to undo changes
2. Use Flyway clean in development (destroys all data)

## Testing

### Local Development

1. Start PostgreSQL locally:
   ```bash
   docker run --name pastpapers-postgres \
     -e POSTGRES_PASSWORD=postgres \
     -p 5432:5432 \
     -d postgres:15-alpine
   ```

2. Run database setup scripts

3. Start application:
   ```bash
   ./gradlew bootRun
   ```

### Integration Tests

The application can use an embedded H2 database for tests or a test PostgreSQL instance:

```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.flyway.enabled=false
```

### Verifying Setup

Check database connectivity and schema:

```sql
-- Connect as DML user
psql -U pastpapers_dml_user -d pastpapers_db

-- List tables
\dt

-- Check audit fields
\d institutions

-- Verify permissions
SELECT * FROM information_schema.table_privileges
WHERE grantee = 'pastpapers_dml_user';

-- Test insert (should work)
INSERT INTO institutions (id, short_name, full_name, created_at, updated_at, created_by, updated_by)
VALUES (gen_random_uuid(), 'TEST', 'Test Institution', NOW(), NOW(), 'test', 'test');

-- Test DDL (should FAIL with permission denied)
CREATE TABLE test_table (id INT);
```

## Troubleshooting

### Common Issues

#### Issue: Migrations fail with "permission denied"

**Solution**: Ensure DDL user credentials are correctly configured in Flyway properties.

```bash
# Check environment variables
echo $DB_DDL_USERNAME
echo $DB_DDL_PASSWORD
```

#### Issue: Application fails to connect at runtime

**Solution**: Verify DML user has correct permissions and credentials.

```bash
# Test connection as DML user
psql -U pastpapers_dml_user -d pastpapers_db -c "SELECT 1"
```

#### Issue: Audit fields not populated

**Solution**: Ensure JPA Auditing is enabled and security context has authenticated user.

```java
// Verify JpaAuditingConfiguration is loaded
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration { ... }
```

#### Issue: File content not stored in papers table

**Solution**: Check that file content is being set before saving the paper entity.

```java
// In PaperRepositoryAdapter
entity.setFileContent(fileBytes);
jpaRepository.save(entity);
```

#### Issue: Cascade deletes not working

**Solution**: Verify foreign key constraints in database:

```sql
-- Check foreign key constraints
SELECT
    tc.constraint_name,
    tc.table_name,
    kcu.column_name,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name,
    rc.delete_rule
FROM information_schema.table_constraints AS tc
JOIN information_schema.key_column_usage AS kcu
  ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage AS ccu
  ON ccu.constraint_name = tc.constraint_name
JOIN information_schema.referential_constraints AS rc
  ON tc.constraint_name = rc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY';
```

### Database Connection Issues

```bash
# Check PostgreSQL is running
systemctl status postgresql

# Check port availability
netstat -tuln | grep 5432

# Check PostgreSQL logs
tail -f /var/log/postgresql/postgresql-*.log
```

### Performance Monitoring

```sql
-- Check slow queries
SELECT * FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;

-- Check table sizes
SELECT
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Check index usage
SELECT
    schemaname,
    tablename,
    indexname,
    idx_scan,
    idx_tup_read,
    idx_tup_fetch
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;
```

## Production Deployment Checklist

- [ ] Change default database passwords
- [ ] Use secrets manager for credentials (AWS Secrets Manager, HashiCorp Vault)
- [ ] Enable SSL/TLS for database connections
- [ ] Configure connection pooling (HikariCP settings)
- [ ] Set up database backups (automated snapshots)
- [ ] Configure monitoring and alerting
- [ ] Restrict database network access (VPC, security groups)
- [ ] Enable database audit logging
- [ ] Review and optimize indexes based on query patterns
- [ ] Set up read replicas for scaling (if needed)
- [ ] Document disaster recovery procedures
- [ ] Test migration rollback procedures

## Additional Resources

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Boot Database Initialization](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization)

## Support

For issues or questions regarding database integration:

1. Check this documentation
2. Review database logs
3. Check application logs
4. Consult the troubleshooting section
5. Open an issue on the project repository
