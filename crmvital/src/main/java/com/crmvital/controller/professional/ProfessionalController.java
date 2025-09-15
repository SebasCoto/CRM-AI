package com.crmvital.controller.professional;

import com.crmvital.model.dto.professionalDTO.ProfessionalDTO;
import com.crmvital.model.dto.userDTO.UserDto;

import com.crmvital.service.professional.ProfessionalService;
import jakarta.mail.MessagingException;

import org.springframework.http.ResponseEntity;
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
    public ProfessionalDTO createProfessional(@RequestBody ProfessionalDTO professionalDTO) throws MessagingException {
        return  professionalService.insertProfessional(professionalDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateProfessional")
    public ProfessionalDTO updateProfessional(@RequestBody ProfessionalDTO professionalDTO) throws MessagingException {
        return  professionalService.updateProfessional(professionalDTO);
    }

    @PutMapping("/toggleStatus")
    public ResponseEntity<UserDto> toggleProfessional(@RequestParam int idProfessional) {
        UserDto response = professionalService.toggleProfessionalUserStatus(idProfessional);
        return ResponseEntity.ok(response);
    }



}
