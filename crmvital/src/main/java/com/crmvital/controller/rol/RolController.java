package com.crmvital.controller.rol;


import com.crmvital.model.dto.rolDTO.RolDTO;
import com.crmvital.model.entity.rol.Rol;
import com.crmvital.service.rol.RolService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rol")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }
    @GetMapping
    public List<Rol> listRol() {
        return rolService.listRol();
    }


    @PostMapping
    public RolDTO saveRol(@RequestBody RolDTO rolDto) {
        return rolService.insertRol(rolDto);
    }

    @DeleteMapping("/{id}")
    public String deleteRol(@PathVariable int id) {
        return rolService.deleteRol(id);
    }




}

