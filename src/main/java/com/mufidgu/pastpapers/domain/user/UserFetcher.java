package com.mufidgu.pastpapers.domain.user;

import com.mufidgu.pastpapers.domain.user.api.FetchUser;
import com.mufidgu.pastpapers.domain.user.spi.Users;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class UserFetcher implements FetchUser {

    private final Users users;

    public User fetch(String googleId) {
        return users.findByGoogleId(googleId)
                .orElse(users.save(
                        User.createFromGoogleId(googleId)
                ));
    }
}
