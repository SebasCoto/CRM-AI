package com.crmvital.controller.Auth;

import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.service.user.AuthService;
import com.crmvital.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        UserDto loggedUser = authService.login(userDto);
        return ResponseEntity.ok(loggedUser);
    }


}
