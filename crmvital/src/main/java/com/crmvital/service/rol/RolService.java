package com.crmvital.service.rol;


import com.crmvital.model.dto.rolDTO.RolDTO;
import com.crmvital.model.entity.rol.Rol;
import com.crmvital.repository.rol.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> listRol() {
        return rolRepository.findAll();
    }

    public RolDTO insertRol(RolDTO rolDto) {
        Rol rol = new Rol();
        rol.setRol_name(rolDto.getRolName());
        Rol saved = rolRepository.save(rol);
        return new RolDTO(saved.getRol_name());
    }


    public String deleteRol(int id) {
        if (rolRepository.existsById(id)) {
            rolRepository.deleteById(id);
            return "Rol eliminado correctamente";
        } else {
            return "Rol no encontrado";
        }
    }



}
