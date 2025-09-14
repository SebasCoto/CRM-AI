package com.crmvital.service.user;

import com.crmvital.model.dto.loginDTO.LoginRequest;
import com.crmvital.model.dto.loginDTO.LoginResponse;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.repository.user.UserRepository;
import com.crmvital.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public UserDto login(UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(),
                        userDto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        UserDto userDtoForToken = new UserDto();
        userDtoForToken.setUsername(userDetails.getUsername());


        String token = jwtUtil.generateToken(userDtoForToken);


        userDtoForToken.setPassword(null);
        userDtoForToken.setToken(token);

        return userDtoForToken;
    }



}
