package com.crmvital.controller.Auth;

import com.crmvital.model.RefreshToken;
import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.loginDTO.LoginRequest;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.repository.user.RefreshTokenRepo;
import com.crmvital.service.user.AuthService;
import com.crmvital.service.user.RefreshTokenService;
import com.crmvital.service.user.UserService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseDTO<UserDto>> login(@RequestBody LoginRequest loginRequest) {
        // loginRequest incluye username, password y optional forceLogin
        ResponseDTO<UserDto> response = authService.login(
                loginRequest.toUserDto(),
                loginRequest.isForceLogin()
        );
        HttpStatus status = response.getSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(response);
    }


    @PostMapping("/resetPassword")
    public ResponseDTO<UserDto> resetPassword(@RequestParam String email) {
        return authService.resetPassword(email);
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
