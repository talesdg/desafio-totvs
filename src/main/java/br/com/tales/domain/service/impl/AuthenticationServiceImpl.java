package br.com.tales.domain.service.impl;

import br.com.tales.domain.dto.AuthDto;
import br.com.tales.domain.model.Users;
import br.com.tales.domain.repository.UserRepository;
import br.com.tales.domain.service.AuthenticationService;
import br.com.tales.infrastracture.exception.UnauthorizedException;
import br.com.tales.infrastracture.response.TokenResponseDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${auth.jwt.token.secret}")
    private String secretKey;

    @Value("${auth.jwt.token.expiration}")
    private Integer tokenExpirationTime;

    @Value("${auth.jwt.refresh-token.expiration}")
    private Integer refreshTokenExpirationTime ;

    @Autowired
    private UserRepository userRepository;
    @Override
    public TokenResponseDto getToken(AuthDto authDto) {
        Users user = userRepository.findByLogin(authDto.login());
        return TokenResponseDto.builder()
                .token(generateTokenJwt(user,tokenExpirationTime))
                .refreshToken(generateTokenJwt(user,refreshTokenExpirationTime))
                .build();
    }

    @Override
    public String validTokenJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    @Override
    public TokenResponseDto getRefreshToken(String refreshToken) {
        String login = validTokenJwt(refreshToken);
        Users user = userRepository.findByLogin(login);

        if (user == null) {
            throw new UnauthorizedException("UnauthorizedException");
        }

        var autentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(autentication);

        return TokenResponseDto
                .builder()
                .token(generateTokenJwt(user,tokenExpirationTime))
                .refreshToken(generateTokenJwt(user,refreshTokenExpirationTime))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    private String generateTokenJwt(Users user, Integer expiration) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(geraDataExpiracao(expiration))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao tentar gerar o token! " +exception.getMessage());
        }
    }

    private Instant geraDataExpiracao(Integer expiration) {
        return LocalDateTime.now()
                .plusHours(expiration)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
