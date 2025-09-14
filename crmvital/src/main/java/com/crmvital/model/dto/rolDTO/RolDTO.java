package com.crmvital.model.dto.rolDTO;

import com.crmvital.model.entity.rol.Rol;
import lombok.Data;

@Data
public class RolDTO {
    private String rolName;

    public RolDTO() {}

    public RolDTO(String rolName) {
        this.rolName = rolName;
    }
}
