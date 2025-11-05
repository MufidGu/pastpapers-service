package com.mufidgu.pastpapers.infrastructure.persistence.entity;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA Entity for User.
 * Maps to the users table in the database.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends AuditableEntity {

    @Id
    @Column(name = "google_id", nullable = false, length = 255)
    private String googleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private InstitutionEntity institution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degree_id")
    private DegreeEntity degree;

    @Column(name = "session_start_date")
    private LocalDate sessionStartDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "session_start_season", length = 20)
    private Season sessionStartSeason;

    @Column(name = "semester")
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "section", length = 20)
    private Section section;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift", length = 20)
    private Shift shift;

    /**
     * Converts this JPA entity to a domain record.
     */
    public User toDomain() {
        return new User(
                googleId,
                institution != null ? institution.getId() : null,
                degree != null ? degree.getId() : null,
                sessionStartDate,
                sessionStartSeason,
                semester,
                section,
                shift
        );
    }

    /**
     * Creates a JPA entity from a domain record (without resolving relationships).
     */
    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .googleId(user.googleId())
                .sessionStartDate(user.sessionStartDate())
                .sessionStartSeason(user.sessionStartSeason())
                .semester(user.semester())
                .section(user.section())
                .shift(user.shift())
                .build();
    }
}
