package com.crmvital.controller.user;


import com.crmvital.model.RefreshToken;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.model.entity.user.User;
import com.crmvital.repository.user.UserRepository;
import com.crmvital.service.user.AuthService;
import com.crmvital.service.user.RefreshTokenService;
import com.crmvital.service.user.UserService;
import com.crmvital.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;


    public UserController(UserService userService, JwtUtil jwtUtil, RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        RefreshToken rt = refreshTokenService.verifyToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido o expirado"));


        User user = userRepository.findById(rt.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRol_name(user.getRol().getRolName());

        String newAccessToken = jwtUtil.generateToken(userDto);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }





}
