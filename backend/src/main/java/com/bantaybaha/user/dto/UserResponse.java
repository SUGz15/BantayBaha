package com.bantaybaha.user.dto;

import com.bantaybaha.user.entity.Role;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Role role,
        Integer reputationScore
) {
}
