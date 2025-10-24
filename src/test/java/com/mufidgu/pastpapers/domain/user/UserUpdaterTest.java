package com.mufidgu.pastpapers.domain.user;

import com.mufidgu.pastpapers.domain.common.enums.Season;
import com.mufidgu.pastpapers.domain.common.enums.Section;
import com.mufidgu.pastpapers.domain.common.enums.Shift;
import com.mufidgu.pastpapers.domain.user.api.FetchUser;
import com.mufidgu.pastpapers.domain.user.spi.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUpdaterTest {

    @Mock
    private Users users;

    @Mock
    private FetchUser userFetcher;

    @InjectMocks
    private UserUpdater userUpdater;

    @Test
    void shouldUpdateUserSuccessfully() {
        // Given
        String googleId = "google123";
        UUID institutionId = UUID.randomUUID();
        UUID degreeId = UUID.randomUUID();
        LocalDate sessionStartDate = LocalDate.of(2024, 9, 1);

        User originalUser = User.createFromGoogleId(googleId);
        User updateData = new User(
                googleId,
                institutionId,
                degreeId,
                sessionStartDate,
                Season.FALL,
                3,
                Section.A,
                Shift.MORNING
        );

        when(userFetcher.fetch(googleId)).thenReturn(originalUser);
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User result = userUpdater.updateUser(updateData);

        // Then
        assertNotNull(result);
        assertEquals(googleId, result.googleId());
        assertEquals(institutionId, result.institutionId());
        assertEquals(degreeId, result.degreeId());
        assertEquals(sessionStartDate, result.sessionStartDate());
        assertEquals(Season.FALL, result.sessionStartSeason());
        assertEquals(3, result.semester());
        assertEquals(Section.A, result.section());
        assertEquals(Shift.MORNING, result.shift());

        verify(userFetcher).fetch(googleId);
        verify(users).save(any(User.class));
    }

    @Test
    void shouldUpdatePartialUserData() {
        // Given
        String googleId = "google123";
        UUID institutionId = UUID.randomUUID();

        User originalUser = User.createFromGoogleId(googleId);
        User updateData = new User(
                googleId,
                institutionId,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(userFetcher.fetch(googleId)).thenReturn(originalUser);
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User result = userUpdater.updateUser(updateData);

        // Then
        assertNotNull(result);
        assertEquals(googleId, result.googleId());
        assertEquals(institutionId, result.institutionId());
        assertNull(result.degreeId());
        assertNull(result.sessionStartDate());

        verify(userFetcher).fetch(googleId);
        verify(users).save(any(User.class));
    }
}
