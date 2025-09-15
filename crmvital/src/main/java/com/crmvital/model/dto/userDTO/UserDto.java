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
    private boolean status;
    public UserDto(String username, String password, int id_rol) {
        this.username = username;
        this.password = password;
        this.id_rol = id_rol;
    }

    public UserDto() {

    }
}
