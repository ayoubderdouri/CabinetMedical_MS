package com.cabinet.auth.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
}
