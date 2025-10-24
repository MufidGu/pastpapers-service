# Domain Test Cases Summary

## Overview
This document summarizes the comprehensive domain test suite created for the pastpapers-service project. All domain services now have thorough unit test coverage using JUnit 5 and Mockito.

## Test Statistics
- **Total Test Classes**: 23
- **Total Test Methods**: 70
- **Test Success Rate**: 100% (70/70 passing)
- **Testing Framework**: JUnit 5 with Mockito
- **Coverage**: All domain services across 6 domain entities

## Domain Test Coverage

### 1. Course Domain (16 tests across 4 classes)
- **CourseAdderTest** (6 tests)
  - Add course successfully
  - Conflict exception when course already exists
  - Not found exception when degree doesn't exist
  - Not found exception when institution doesn't exist
  - Add course with multiple degrees
  - Add course with multiple institutions

- **CourseDeleterTest** (2 tests)
  - Delete course successfully
  - Not found exception when course doesn't exist

- **CourseListerTest** (2 tests)
  - Return all courses
  - Return empty list when no courses

- **CourseUpdaterTest** (6 tests)
  - Update course successfully
  - Not found exception when course doesn't exist
  - Conflict exception when updated name exists for different course
  - Allow update with same name for same course
  - Not found exception when degree doesn't exist
  - Not found exception when institution doesn't exist

### 2. Degree Domain (13 tests across 4 classes)
- **DegreeAdderTest** (4 tests)
  - Add degree successfully
  - Conflict exception when degree already exists
  - Not found exception when institution doesn't exist
  - Add degree with multiple institutions

- **DegreeDeleterTest** (2 tests)
  - Delete degree successfully
  - Not found exception when degree doesn't exist

- **DegreeListerTest** (2 tests)
  - Return all degrees
  - Return empty list when no degrees

- **DegreeUpdaterTest** (5 tests)
  - Update degree successfully
  - Not found exception when degree doesn't exist
  - Conflict exception when updated name exists for different degree
  - Allow update with same name for same degree
  - Not found exception when institution doesn't exist

### 3. Institution Domain (10 tests across 4 classes)
- **InstitutionAdderTest** (2 tests)
  - Add institution successfully
  - Conflict exception when institution already exists

- **InstitutionDeleterTest** (2 tests)
  - Delete institution successfully
  - Not found exception when institution doesn't exist

- **InstitutionListerTest** (2 tests)
  - Return all institutions
  - Return empty list when no institutions

- **InstitutionUpdaterTest** (4 tests)
  - Update institution successfully
  - Not found exception when institution doesn't exist
  - Conflict exception when updated name exists for different institution
  - Allow update with same name for same institution

### 4. Instructor Domain (15 tests across 4 classes)
- **InstructorAdderTest** (5 tests)
  - Add instructor successfully
  - Conflict exception when instructor already exists
  - Not found exception when course doesn't exist
  - Not found exception when institution doesn't exist
  - Add instructor with multiple courses and institutions

- **InstructorDeleterTest** (2 tests)
  - Delete instructor successfully
  - Not found exception when instructor doesn't exist

- **InstructorListerTest** (2 tests)
  - Return all instructors
  - Return empty list when no instructors

- **InstructorUpdaterTest** (6 tests)
  - Update instructor successfully
  - Not found exception when instructor doesn't exist
  - Conflict exception when updated name exists for different instructor
  - Allow update with same name for same instructor
  - Not found exception when course doesn't exist
  - Not found exception when institution doesn't exist

### 5. Paper Domain (12 tests across 5 classes)
- **PaperUploaderTest** (2 tests)
  - Upload paper successfully
  - Internal server exception on IO exception

- **PaperDeleterTest** (3 tests)
  - Delete paper successfully
  - Not found exception when paper doesn't exist
  - Delete paper even when file storage fails

- **PaperUpdaterTest** (2 tests)
  - Update paper successfully
  - Not found exception when paper doesn't exist

- **PaperListerTest** (2 tests)
  - Return all papers
  - Return empty list when no papers

- **PaperDownloaderTest** (3 tests)
  - Download paper successfully
  - Not found exception when paper doesn't exist
  - Internal server exception on IO exception

### 6. User Domain (4 tests across 2 classes)
- **UserFetcherTest** (2 tests)
  - Fetch existing user
  - Create and save new user when not found

- **UserUpdaterTest** (2 tests)
  - Update user successfully
  - Update partial user data

## Test Patterns Used

### Mocking Strategy
- All SPI (Service Provider Interface) dependencies are mocked using Mockito
- Repository interfaces (Courses, Degrees, Institutions, etc.) are mocked
- File storage operations are mocked for Paper domain tests

### Validation Coverage
Tests validate:
- **Success paths**: Normal operations complete successfully
- **Business rules**: Duplicate prevention, relationship validation
- **Error handling**: Proper exceptions thrown for invalid operations
- **Edge cases**: Empty lists, null values, multiple entity relationships

### Exception Testing
Comprehensive coverage of domain exceptions:
- `NotFoundException`: When entities don't exist
- `ConflictException`: When duplicates are attempted
- `InternalServerException`: When system errors occur

## Test Execution
All tests can be run with:
```bash
./gradlew test --tests "com.mufidgu.pastpapers.domain.*"
```

## Notes
- One pre-existing test (`ApplicationTests.whenAdminRole_thenCanAddInstitution`) fails due to missing configuration, but this is unrelated to domain tests
- All 70 domain tests pass successfully (100% success rate)
- Tests follow consistent patterns and naming conventions
- Each test is independent and can be run in isolation
