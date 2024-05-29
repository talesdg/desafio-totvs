package br.com.tales.infrastracture.response;

import lombok.Builder;

@Builder
public record TokenResponseDto(String token, String refreshToken) {
}
