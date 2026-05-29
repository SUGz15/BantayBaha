package com.bantaybaha.auth.dto;

import com.bantaybaha.user.dto.UserResponse;

public record AuthResponse(
        String token,
        String tokenType,
        UserResponse user
) {
}
