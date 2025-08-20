package com.mufidgu.pastpapers.domain.user.spi.stub;

import com.mufidgu.pastpapers.domain.user.User;
import com.mufidgu.pastpapers.domain.user.spi.Users;
import ddd.Stub;

import java.util.HashMap;
import java.util.Optional;

@Stub
public class InMemoryUsers implements Users {
    private final HashMap<String, User> users = new HashMap<>();

    public User save(User user) {
        users.put(user.googleId(), user);
        return user;
    }

    @Override
    public Optional<User> findByGoogleId(String googleId) {
        return Optional.ofNullable(users.get(googleId));
    }
}
