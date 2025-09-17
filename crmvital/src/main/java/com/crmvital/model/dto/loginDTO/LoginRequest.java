package com.crmvital.model.dto.loginDTO;

import com.crmvital.model.dto.userDTO.UserDto;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private boolean forceLogin;

    // getters y setters

    public UserDto toUserDto() {
        UserDto dto = new UserDto();
        dto.setUsername(this.username);
        dto.setPassword(this.password);
        return dto;
    }
}
