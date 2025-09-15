package com.crmvital.repository.rol;

import com.crmvital.model.entity.rol.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol,Integer> {

    Optional<Rol> findByRolName(String rolName);
}
