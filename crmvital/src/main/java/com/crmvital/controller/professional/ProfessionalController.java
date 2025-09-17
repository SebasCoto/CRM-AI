package com.crmvital.controller.professional;

import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.professionalDTO.ProfessionalDTO;
import com.crmvital.model.dto.userDTO.UserDto;

import com.crmvital.service.professional.ProfessionalService;
import jakarta.mail.MessagingException;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RestControllerAdvice
@RequestMapping("/professional")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createProfessional")
    public ResponseDTO<ProfessionalDTO> createProfessional(@RequestBody ProfessionalDTO professionalDTO) throws MessagingException {
        ProfessionalDTO created = professionalService.insertProfessional(professionalDTO);

        ResponseDTO<ProfessionalDTO> response = new ResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Profesional creado correctamente");
        response.setObject(created);

        return response;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateProfessional")
    public ResponseDTO<ProfessionalDTO> updateProfessional(@RequestBody ProfessionalDTO professionalDTO) throws MessagingException {
        ProfessionalDTO updated =  professionalService.updateProfessional(professionalDTO);

        ResponseDTO<ProfessionalDTO> response = new ResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Profesional actualizado correctamente");
        response.setObject(updated);

        return response;
    }

    @PutMapping("/toggleStatus")
    public ResponseDTO<UserDto> toggleProfessional(@RequestParam int idProfessional) {
        UserDto toggleStatus = professionalService.toggleProfessionalUserStatus(idProfessional);

        ResponseDTO<UserDto> response = new ResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Estado del usuario actualizado correctamente");
        response.setObject(toggleStatus);

        return response;
    }



}
