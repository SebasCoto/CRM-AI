package com.crmvital.controller.patient;

import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.patientDTO.PatientCreateDTO;
import com.crmvital.model.dto.professionalDTO.ProfessionalDTO;
import com.crmvital.projection.PatientProjection;
import com.crmvital.service.patient.PatientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RestControllerAdvice
@RequestMapping("/patients")
public class PatientController {

    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ASSISTANT')")
    @PostMapping
    public ResponseDTO<PatientCreateDTO> createPatient(@RequestBody PatientCreateDTO patientCreateDTO) {
        return patientService.insertPatient(patientCreateDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ASSISTANT')")
    @PutMapping
    public ResponseDTO<PatientCreateDTO> updatePatient(@RequestBody PatientCreateDTO patientCreateDTO) {
        return patientService.updatePatient(patientCreateDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ASSISTANT')")
    @PatchMapping("/{id}/status")
    public ResponseDTO<PatientCreateDTO> changeStatus(@PathVariable("id") int id){
        return patientService.changeStatus(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ASSISTANT')")
    @GetMapping
    public ResponseDTO<List<PatientProjection>> getPatientsResponseDTO(){
        return patientService.getAllPatients();
    }


}
