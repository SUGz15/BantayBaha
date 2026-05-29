package com.bantaybaha.user.mapper;

import com.bantaybaha.user.dto.UserResponse;
import com.bantaybaha.user.entity.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getReputationScore()
        );
    }
}
