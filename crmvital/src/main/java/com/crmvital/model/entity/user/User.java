package com.crmvital.model.entity.user;

import com.crmvital.model.entity.rol.Rol;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "auth_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name="password_auth")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    private Rol rol;

    @Column(name = "status")
    private boolean status;

    @Column(name = "is_temporal_pass")
    private Boolean isTempPass;

    @Column(name = "date_expiration_temp_pass")
    private LocalDateTime dateExpirationTempPass;




}
