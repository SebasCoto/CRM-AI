package com.crmvital.controller.assistant;


import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.assisDTO.AssisDTO;
import com.crmvital.model.dto.professionalDTO.ProfessionalDTO;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.model.entity.assistant.Assistant;
import com.crmvital.projection.AssistantProjection;
import com.crmvital.projection.ProfessionalProjection;
import com.crmvital.repository.assistant.AssistantRepo;
import com.crmvital.service.assistant.AssistService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RestControllerAdvice
@RequestMapping("/assistants")
public class AssistantController {

    private final AssistService assistService;

    public AssistantController(AssistService assistService, AssistantRepo assistantRepo) {
        this.assistService = assistService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseDTO<AssisDTO> createAssistant(@RequestBody AssisDTO assisDTO) throws MessagingException {
        return assistService.insertAssistant(assisDTO);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseDTO<AssisDTO> updateAssistant(@RequestBody AssisDTO assisDTO) throws MessagingException {
        return assistService.updateAssistant(assisDTO);
    }

    @PatchMapping("/toggle-status/{id}")
    public ResponseEntity<ResponseDTO<UserDto>> toggleAssistantUserStatus(@PathVariable int id) {
        ResponseDTO<UserDto> response = assistService.toggleAssistantUserStatus(id);


        HttpStatus status = response.getObject() != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping
    public ResponseDTO<List<AssistantProjection>> getAllAssistants() {
        return assistService.getAllAssistants();
    }



}
