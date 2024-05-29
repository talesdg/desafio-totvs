package br.com.tales.application;

import br.com.tales.domain.dto.AuthDto;
import br.com.tales.domain.dto.RequestRefreshDto;
import br.com.tales.domain.service.AuthenticationService;
import br.com.tales.infrastracture.response.TokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto auth(@RequestBody AuthDto authDto) {

        var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.login(), authDto.senha());

        authenticationManager.authenticate(usuarioAutenticationToken);

        return authenticationService.getToken(authDto);
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto authRefreshToken(@RequestBody RequestRefreshDto requestRefreshDto) {
        return authenticationService.getRefreshToken(requestRefreshDto.refreshToken());
    }
}
