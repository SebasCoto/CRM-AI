package com.crmvital.controller.professional;

import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.professionalDTO.ProfessionalDTO;
import com.crmvital.model.dto.userDTO.UserDto;

import com.crmvital.projection.ProfessionalProjection;
import com.crmvital.service.professional.ProfessionalService;
import jakarta.mail.MessagingException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<ResponseDTO<UserDto>> toggleProfessionalUserStatus(@PathVariable int id) {
        ResponseDTO<UserDto> response = professionalService.toggleProfessionalUserStatus(id);


        HttpStatus status = response.getObject() != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping
    public ResponseDTO<List<ProfessionalProjection>> getProfessionals() {
        return professionalService.getAllProfessionals();
    }



}
