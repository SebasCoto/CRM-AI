package com.crmvital.model.dto.userDTO;

import com.crmvital.model.entity.rol.Rol;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String username;
    private String password;
    private int id_rol;
    private String rol_name;
    private String token;
    private String refreshToken;

    private boolean status;
    private String oldPassword;
    private String confirmPassword;
    private String newPassword;

    private String email;
    public UserDto(String username, String password, int id_rol, String rol_name, String token, boolean status,
                   String email,
                   String oldPassword, String newPassword,
                   String confirmPassword) {
        this.username = username;
        this.password = password;
        this.id_rol = id_rol;
        this.rol_name = rol_name;
        this.token = token;
        this.status = status;
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public UserDto() {

    }
}
