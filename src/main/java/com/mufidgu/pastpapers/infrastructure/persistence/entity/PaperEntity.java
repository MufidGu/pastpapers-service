package com.mufidgu.pastpapers.infrastructure.persistence.entity;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.common.enums.Type;
import com.mufidgu.pastpapers.domain.paper.Paper;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA Entity for Paper.
 * Maps to the papers table in the database.
 */
@Entity
@Table(name = "papers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaperEntity extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private InstructorEntity instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private InstitutionEntity institution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degree_id")
    private DegreeEntity degree;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift", length = 20)
    private Shift shift;

    @Column(name = "semester")
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "section", length = 20)
    private Section section;

    @Column(name = "year")
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", length = 20)
    private Season season;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "file_name", nullable = false, length = 500)
    private String fileName;

    @Lob
    @Column(name = "file_content", nullable = false)
    private byte[] fileContent;

    /**
     * Converts this JPA entity to a domain record.
     */
    public Paper toDomain() {
        return new Paper(
                id,
                user != null ? user.getGoogleId() : null,
                instructor != null ? instructor.getId() : null,
                course != null ? course.getId() : null,
                type,
                institution != null ? institution.getId() : null,
                degree != null ? degree.getId() : null,
                shift,
                semester,
                section,
                year,
                season,
                date,
                fileName
        );
    }

    /**
     * Creates a JPA entity from a domain record (without resolving relationships).
     */
    public static PaperEntity fromDomain(Paper paper) {
        return PaperEntity.builder()
                .id(paper.id())
                .type(paper.type())
                .shift(paper.shift())
                .semester(paper.semester())
                .section(paper.section())
                .year(paper.year())
                .season(paper.season())
                .date(paper.date())
                .fileName(paper.fileName())
                .build();
    }
}
