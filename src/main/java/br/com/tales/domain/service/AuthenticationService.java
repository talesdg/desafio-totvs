package br.com.tales.domain.service;

import br.com.tales.domain.dto.AuthDto;
import br.com.tales.infrastracture.response.TokenResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {
    TokenResponseDto getToken(AuthDto authDto);
    String validTokenJwt(String token);
    TokenResponseDto getRefreshToken(String s);
}
