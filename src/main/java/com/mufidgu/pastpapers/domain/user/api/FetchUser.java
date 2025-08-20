package com.mufidgu.pastpapers.domain.user.api;

import com.mufidgu.pastpapers.domain.user.User;

public interface FetchUser {
    User fetch(String googleId);
}
