package com.crmvital.controller.Auth;

import com.crmvital.model.RefreshToken;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.repository.user.RefreshTokenRepo;
import com.crmvital.service.user.AuthService;
import com.crmvital.service.user.RefreshTokenService;
import com.crmvital.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepo refreshTokenRepo;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, RefreshTokenRepo refreshTokenRepo) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        UserDto loggedUser = authService.login(userDto);

        RefreshToken refreshToken = refreshTokenService.getRefreshToken(userDto.getId());

        refreshTokenRepo.save(refreshToken);
        return ResponseEntity.ok(Map.of(
                "accessToken", loggedUser.getToken(),
                "refreshToken", refreshToken.getToken()
        ));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String refreshToken) {
        Optional<RefreshToken> rtOpt = refreshTokenService.verifyToken(refreshToken);

        if (rtOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Refresh token inválido o ya eliminado"));
        }
        refreshTokenService.deleteToken(refreshToken);
        return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
    }



}
