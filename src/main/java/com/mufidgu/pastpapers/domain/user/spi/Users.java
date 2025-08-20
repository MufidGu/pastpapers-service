package com.mufidgu.pastpapers.domain.user.spi;

import com.mufidgu.pastpapers.domain.user.User;

import java.util.Optional;

public interface Users {
    User save(User user);

    Optional<User> findByGoogleId(String googleId);
}
