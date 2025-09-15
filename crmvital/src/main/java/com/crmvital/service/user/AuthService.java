package com.crmvital.service.user;

import com.crmvital.model.RefreshToken;
import com.crmvital.model.dto.loginDTO.LoginRequest;
import com.crmvital.model.dto.loginDTO.LoginResponse;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.model.entity.user.User;
import com.crmvital.repository.user.UserRepository;
import com.crmvital.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    public UserDto login(UserDto userDto) {
        // Autenticación
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(),
                        userDto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (!user.isStatus()) {
            throw new DisabledException("Usuario desactivado");
        }

        // Generar token
        UserDto userDtoForToken = new UserDto();
        userDtoForToken.setId(user.getId());
        userDtoForToken.setUsername(user.getUsername());
        userDtoForToken.setRol_name(user.getRol().getRolName());

        String token = jwtUtil.generateToken(userDtoForToken);
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(user.getId());

        userDtoForToken.setPassword(null);
        userDtoForToken.setToken(token);

        return userDtoForToken;
    }











}
