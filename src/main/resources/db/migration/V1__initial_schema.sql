-- Initial schema for pastpapers-service database
-- Creates all tables with audit fields and proper foreign key constraints with cascading rules

-- Create institutions table (base table with no dependencies)
CREATE TABLE institutions (
    id UUID PRIMARY KEY,
    short_name VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL
);

CREATE INDEX idx_institutions_short_name ON institutions(short_name);

-- Create degrees table
CREATE TABLE degrees (
    id UUID PRIMARY KEY,
    short_name VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL
);

CREATE INDEX idx_degrees_short_name ON degrees(short_name);

-- Create degree_institutions junction table
CREATE TABLE degree_institutions (
    degree_id UUID NOT NULL,
    institution_id UUID NOT NULL,
    PRIMARY KEY (degree_id, institution_id),
    CONSTRAINT fk_degree_institutions_degree
        FOREIGN KEY (degree_id) REFERENCES degrees(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_degree_institutions_institution
        FOREIGN KEY (institution_id) REFERENCES institutions(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_degree_institutions_degree ON degree_institutions(degree_id);
CREATE INDEX idx_degree_institutions_institution ON degree_institutions(institution_id);

-- Create courses table
CREATE TABLE courses (
    id UUID PRIMARY KEY,
    short_name VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL
);

CREATE INDEX idx_courses_short_name ON courses(short_name);

-- Create course_degrees junction table
CREATE TABLE course_degrees (
    course_id UUID NOT NULL,
    degree_id UUID NOT NULL,
    PRIMARY KEY (course_id, degree_id),
    CONSTRAINT fk_course_degrees_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_course_degrees_degree
        FOREIGN KEY (degree_id) REFERENCES degrees(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_course_degrees_course ON course_degrees(course_id);
CREATE INDEX idx_course_degrees_degree ON course_degrees(degree_id);

-- Create course_institutions junction table
CREATE TABLE course_institutions (
    course_id UUID NOT NULL,
    institution_id UUID NOT NULL,
    PRIMARY KEY (course_id, institution_id),
    CONSTRAINT fk_course_institutions_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_course_institutions_institution
        FOREIGN KEY (institution_id) REFERENCES institutions(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_course_institutions_course ON course_institutions(course_id);
CREATE INDEX idx_course_institutions_institution ON course_institutions(institution_id);

-- Create instructors table
CREATE TABLE instructors (
    id UUID PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL
);

CREATE INDEX idx_instructors_full_name ON instructors(full_name);

-- Create instructor_courses junction table
CREATE TABLE instructor_courses (
    instructor_id UUID NOT NULL,
    course_id UUID NOT NULL,
    PRIMARY KEY (instructor_id, course_id),
    CONSTRAINT fk_instructor_courses_instructor
        FOREIGN KEY (instructor_id) REFERENCES instructors(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_instructor_courses_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_instructor_courses_instructor ON instructor_courses(instructor_id);
CREATE INDEX idx_instructor_courses_course ON instructor_courses(course_id);

-- Create instructor_institutions junction table
CREATE TABLE instructor_institutions (
    instructor_id UUID NOT NULL,
    institution_id UUID NOT NULL,
    PRIMARY KEY (instructor_id, institution_id),
    CONSTRAINT fk_instructor_institutions_instructor
        FOREIGN KEY (instructor_id) REFERENCES instructors(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_instructor_institutions_institution
        FOREIGN KEY (institution_id) REFERENCES institutions(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_instructor_institutions_instructor ON instructor_institutions(instructor_id);
CREATE INDEX idx_instructor_institutions_institution ON instructor_institutions(institution_id);

-- Create users table
CREATE TABLE users (
    google_id VARCHAR(255) PRIMARY KEY,
    institution_id UUID,
    degree_id UUID,
    session_start_date DATE,
    session_start_season VARCHAR(20),
    semester INTEGER,
    section VARCHAR(20),
    shift VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    CONSTRAINT fk_users_institution
        FOREIGN KEY (institution_id) REFERENCES institutions(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_users_degree
        FOREIGN KEY (degree_id) REFERENCES degrees(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_users_institution ON users(institution_id);
CREATE INDEX idx_users_degree ON users(degree_id);

-- Create papers table
CREATE TABLE papers (
    id UUID PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    instructor_id UUID,
    course_id UUID,
    type VARCHAR(20),
    institution_id UUID,
    degree_id UUID,
    shift VARCHAR(20),
    semester INTEGER,
    section VARCHAR(20),
    year INTEGER,
    season VARCHAR(20),
    date DATE,
    file_name VARCHAR(500) NOT NULL,
    file_content BYTEA NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    CONSTRAINT fk_papers_user
        FOREIGN KEY (user_id) REFERENCES users(google_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_papers_instructor
        FOREIGN KEY (instructor_id) REFERENCES instructors(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_papers_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_papers_institution
        FOREIGN KEY (institution_id) REFERENCES institutions(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_papers_degree
        FOREIGN KEY (degree_id) REFERENCES degrees(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_papers_user ON papers(user_id);
CREATE INDEX idx_papers_instructor ON papers(instructor_id);
CREATE INDEX idx_papers_course ON papers(course_id);
CREATE INDEX idx_papers_institution ON papers(institution_id);
CREATE INDEX idx_papers_degree ON papers(degree_id);
CREATE INDEX idx_papers_year_season ON papers(year, season);

-- Comments explaining cascading rules
COMMENT ON TABLE institutions IS 'Base table for educational institutions';
COMMENT ON TABLE degrees IS 'Academic degrees offered by institutions';
COMMENT ON TABLE courses IS 'Courses offered across degrees and institutions';
COMMENT ON TABLE instructors IS 'Faculty members teaching courses';
COMMENT ON TABLE users IS 'Students registered in the system via Google OAuth';
COMMENT ON TABLE papers IS 'Past exam papers uploaded by students';

-- Cascading rules explanation:
-- 1. Junction tables (many-to-many): ON DELETE CASCADE - removing parent removes junction
-- 2. Papers -> User: ON DELETE CASCADE - removing user removes their papers
-- 3. Users/Papers -> Institution/Degree/Course/Instructor: ON DELETE SET NULL - preserve data but clear reference
