package com.mufidgu.pastpapers.infrastructure.persistence.adapter;

import com.mufidgu.pastpapers.domain.paper.Paper;
import com.mufidgu.pastpapers.domain.paper.spi.FileStorage;
import com.mufidgu.pastpapers.domain.paper.spi.Papers;
import com.mufidgu.pastpapers.infrastructure.persistence.entity.*;
import com.mufidgu.pastpapers.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA adapter implementation for the Papers SPI.
 * Handles file storage in the database along with paper metadata.
 * Also implements FileStorage SPI for backward compatibility.
 * Disabled in test profile to allow stub implementations.
 */
@Repository
@Primary
@Profile("!test")
@RequiredArgsConstructor
@Transactional
public class PaperRepositoryAdapter implements Papers, FileStorage {

    private final PaperJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final InstructorJpaRepository instructorJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final InstitutionJpaRepository institutionJpaRepository;
    private final DegreeJpaRepository degreeJpaRepository;

    @Override
    public Paper save(Paper paper) {
        PaperEntity entity = jpaRepository.findById(paper.id())
                .orElse(PaperEntity.fromDomain(paper));

        // Update basic fields
        entity.setType(paper.type());
        entity.setShift(paper.shift());
        entity.setSemester(paper.semester());
        entity.setSection(paper.section());
        entity.setYear(paper.year());
        entity.setSeason(paper.season());
        entity.setDate(paper.date());
        entity.setFileName(paper.fileName());

        // Resolve user relationship (required)
        if (paper.userId() != null) {
            UserEntity user = userJpaRepository.findById(paper.userId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + paper.userId()));
            entity.setUser(user);
        }

        // Resolve optional relationships
        if (paper.instructorId() != null) {
            InstructorEntity instructor = instructorJpaRepository.findById(paper.instructorId())
                    .orElse(null);
            entity.setInstructor(instructor);
        } else {
            entity.setInstructor(null);
        }

        if (paper.courseId() != null) {
            CourseEntity course = courseJpaRepository.findById(paper.courseId())
                    .orElse(null);
            entity.setCourse(course);
        } else {
            entity.setCourse(null);
        }

        if (paper.institutionId() != null) {
            InstitutionEntity institution = institutionJpaRepository.findById(paper.institutionId())
                    .orElse(null);
            entity.setInstitution(institution);
        } else {
            entity.setInstitution(null);
        }

        if (paper.degreeId() != null) {
            DegreeEntity degree = degreeJpaRepository.findById(paper.degreeId())
                    .orElse(null);
            entity.setDegree(degree);
        } else {
            entity.setDegree(null);
        }

        PaperEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paper> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(PaperEntity::toDomain);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paper> findAll() {
        return jpaRepository.findAll().stream()
                .map(PaperEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paper> findByIdAndUserId(UUID id, String userId) {
        return jpaRepository.findByIdAndUserId(id, userId)
                .map(PaperEntity::toDomain);
    }

    // FileStorage implementation

    @Override
    public void store(byte[] file, String fileName) {
        // Find the paper entity by fileName and store the file content
        // Note: This assumes fileName is unique. In practice, you might want to use paper ID instead.
        Optional<PaperEntity> paperOpt = jpaRepository.findAll().stream()
                .filter(p -> p.getFileName().equals(fileName))
                .findFirst();

        if (paperOpt.isPresent()) {
            PaperEntity paper = paperOpt.get();
            paper.setFileContent(file);
            jpaRepository.save(paper);
        } else {
            throw new IllegalArgumentException("Paper not found with fileName: " + fileName);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] retrieve(String fileName) throws IOException {
        return jpaRepository.findAll().stream()
                .filter(p -> p.getFileName().equals(fileName))
                .findFirst()
                .map(PaperEntity::getFileContent)
                .orElseThrow(() -> new IOException("File not found: " + fileName));
    }

    @Override
    public void delete(String fileName) {
        jpaRepository.findAll().stream()
                .filter(p -> p.getFileName().equals(fileName))
                .findFirst()
                .ifPresent(paper -> jpaRepository.deleteById(paper.getId()));
    }
}
