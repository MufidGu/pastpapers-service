package com.mufidgu.pastpapers.infrastructure.controller.user;

import com.mufidgu.pastpapers.domain.user.User;
import com.mufidgu.pastpapers.domain.user.api.FetchUser;
import com.mufidgu.pastpapers.domain.user.api.UpdateUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final FetchUser userFetcher;
    private final UpdateUser userUpdater;

    @GetMapping("/get")
    public ResponseEntity<UserResource> get(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                UserResource.from(
                        userFetcher.fetch(jwt.getSubject())
                )
        );
    }

    @PutMapping("/update")
    public ResponseEntity<UserResource> update(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserRequest request
    ) {
        User user = userUpdater.updateUser(
                request.toUser(
                        jwt.getSubject()
                )
        );
        return ResponseEntity.ok(
                UserResource.from(user)
        );
    }
}
