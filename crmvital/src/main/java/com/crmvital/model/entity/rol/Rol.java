package com.crmvital.model.entity.rol;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Rol {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private int id;

    @Column(name = "rol_name")
    private String rolName;

}
