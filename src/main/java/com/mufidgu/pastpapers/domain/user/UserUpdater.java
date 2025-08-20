package com.mufidgu.pastpapers.domain.user;

import com.mufidgu.pastpapers.domain.user.api.FetchUser;
import com.mufidgu.pastpapers.domain.user.api.UpdateUser;
import com.mufidgu.pastpapers.domain.user.spi.Users;
import ddd.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class UserUpdater implements UpdateUser {

    private final Users users;
    private final FetchUser userFetcher;

    public User updateUser(User update) {
        User orignalUser = userFetcher.fetch(update.googleId());

        return users.save(
                orignalUser.updateWith(update)
        );
    }
}
