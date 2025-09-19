package com.crmvital.service.patient;

import com.crmvital.model.dto.IdCardDTO;
import com.crmvital.model.dto.IdCardResponseDTO;
import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.patientDTO.PatientCreateDTO;
import com.crmvital.model.entity.patient.Patient;
import com.crmvital.projection.PatientProjection;
import com.crmvital.projection.ProfessionalProjection;
import com.crmvital.repository.patient.PatientRepository;
import com.crmvital.service.IdService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.crmvital.util.UsersUtil.capitalizeWords;

@Service
public class PatientService {

    private PatientRepository patientRepository;
    private final IdService idService;
    public PatientService(PatientRepository patientRepository,IdService idService) {
        this.patientRepository = patientRepository;
        this.idService = idService;
    }



    @Transactional
    public ResponseDTO<PatientCreateDTO> insertPatient(PatientCreateDTO patientCreateDTO) {


        try {
            Patient patient = getPatient(patientCreateDTO);
            patientCreateDTO.setName(patient.getName());
            patientCreateDTO.setFirstLastName(patient.getFirstLastName());
            patientCreateDTO.setSecondLastName(patient.getSecondLastName());
            validatePatient(patientCreateDTO);


            patient.setCreationDate(LocalDateTime.now());
            patient.setStatus(true);
            Patient savedPatient = patientRepository.save(patient);

            ResponseDTO<PatientCreateDTO> responseDTO = new ResponseDTO<>();
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Se ha registrado el paciente correctamente");
            responseDTO.setObject(patientCreateDTO);

            return responseDTO;

        } catch (IllegalArgumentException ex) {
            ResponseDTO<PatientCreateDTO> responseDTO = new ResponseDTO<>();
            responseDTO.setSuccess(false);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setObject(null);
            return responseDTO;

        } catch (DataAccessException ex) {
            ResponseDTO<PatientCreateDTO> responseDTO = new ResponseDTO<>();
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error al registrar el paciente: " + ex.getMessage());
            responseDTO.setObject(null);
            return responseDTO;
        }

    }


    private Patient getPatient(PatientCreateDTO patientCreateDTO) {
        Patient patient = new Patient();
        boolean obtainedName = false;

        IdCardResponseDTO response = idService.consultId(patientCreateDTO.getIdCard());

        if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
            IdCardDTO result = response.getResults().get(0);

            patient.setName(capitalizeWords(result.getFirstname1()));
            patient.setFirstLastName(capitalizeWords(result.getLastname1()));
            patient.setSecondLastName(capitalizeWords(result.getLastname2()));

            obtainedName = true;
        } else if (patientCreateDTO.getName() != null
                && patientCreateDTO.getFirstLastName() != null) {
            patient.setName(patientCreateDTO.getName());
            patient.setFirstLastName(patientCreateDTO.getFirstLastName());
            patient.setSecondLastName(patientCreateDTO.getSecondLastName());
            obtainedName = true;
        }

        if (!obtainedName) {
            throw new IllegalArgumentException(
                    "No se pudo obtener el nombre automáticamente. Por favor ingrese el nombre manualmente."
            );
        }

        patient.setAddress(patientCreateDTO.getAddress());
        patient.setEmail(patientCreateDTO.getEmail());
        patient.setPhoneNumber(patientCreateDTO.getPhoneNumber());
        patient.setBirthDate(patientCreateDTO.getDateOfBirth());
        patient.setSex(patientCreateDTO.getGender());
        patient.setIdCard(patientCreateDTO.getIdCard());

        return patient;
    }







    private void validatePatient(PatientCreateDTO dto) {
        if (dto.getIdCard() == null || dto.getIdCard().length() < 9) {
            throw new IllegalArgumentException("La cédula no tiene el número de caracteres correcto");
        }

        if (dto.getIdCard() == null || dto.getIdCard().length() > 9) {
            throw new IllegalArgumentException("La cédula no tiene el número de caracteres correcto");
        }

        if (patientRepository.existsByIdCard(dto.getIdCard())) {
            throw new IllegalArgumentException("La cédula ya existe en la base de datos");
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }

        if (patientRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (patientRepository.existsByNameAndFirstLastNameAndSecondLastName(
                dto.getName(),
                dto.getFirstLastName(),
                dto.getSecondLastName()
        )) {
            throw new IllegalArgumentException("El nombre completo del profesional ya está registrado");
        }

        if (!dto.getIdCard().matches("\\d{9}")) {
            throw new IllegalArgumentException("La cédula debe tener 9 dígitos numéricos");
        }

        if (!dto.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del Paciente es obligatorio");
        }
        if (dto.getFirstLastName() == null || dto.getFirstLastName().isBlank()) {
            throw new IllegalArgumentException("El primer apellido es obligatorio");
        }

    }


    @Transactional
    public ResponseDTO<PatientCreateDTO> updatePatient(PatientCreateDTO patientCreateDTO) {
        Patient patient = patientRepository.findById(patientCreateDTO.getId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        boolean changed = false;

        if (!Objects.equals(patient.getName(), patientCreateDTO.getName())) {
            patient.setName(patientCreateDTO.getName());
            changed = true;
        }
        if (!Objects.equals(patient.getFirstLastName(), patientCreateDTO.getFirstLastName())) {
            patient.setFirstLastName(patientCreateDTO.getFirstLastName());
            changed = true;
        }
        if (!Objects.equals(patient.getSecondLastName(), patientCreateDTO.getSecondLastName())) {
            patient.setSecondLastName(patientCreateDTO.getSecondLastName());
            changed = true;
        }
        if (!Objects.equals(patient.getEmail(), patientCreateDTO.getEmail())) {
            patient.setEmail(patientCreateDTO.getEmail());
            changed = true;
        }
        if (!Objects.equals(patient.getPhoneNumber(), patientCreateDTO.getPhoneNumber())) {
            patient.setPhoneNumber(patientCreateDTO.getPhoneNumber());
            changed = true;
        }
        if (!Objects.equals(patient.getBirthDate(), patientCreateDTO.getDateOfBirth())) {
            patient.setBirthDate(patientCreateDTO.getDateOfBirth());
            changed = true;
        }
        if (!Objects.equals(patient.getSex(), patientCreateDTO.getGender())) {
            patient.setSex(patientCreateDTO.getGender());
            changed = true;
        }
        if (!Objects.equals(patient.getIdCard(), patientCreateDTO.getIdCard())) {
            patient.setIdCard(patientCreateDTO.getIdCard());
            changed = true;
        }

        ResponseDTO<PatientCreateDTO> resultDTO = new ResponseDTO<>();

        if (changed) {
            patientRepository.save(patient);
            resultDTO.setMessage("Paciente actualizado exitosamente");
            resultDTO.setSuccess(true);
        } else {
            resultDTO.setMessage("No hubo cambios en el paciente");
            resultDTO.setSuccess(false);
        }

        // Mapear paciente actualizado al DTO (opcional)
        PatientCreateDTO updatedDTO = new PatientCreateDTO();
        updatedDTO.setId(patient.getId());
        updatedDTO.setName(patient.getName());
        updatedDTO.setFirstLastName(patient.getFirstLastName());
        updatedDTO.setSecondLastName(patient.getSecondLastName());
        updatedDTO.setEmail(patient.getEmail());
        updatedDTO.setPhoneNumber(patient.getPhoneNumber());
        updatedDTO.setDateOfBirth(patient.getBirthDate());
        updatedDTO.setGender(patient.getSex());
        updatedDTO.setIdCard(patient.getIdCard());

        resultDTO.setObject(updatedDTO);

        return resultDTO;
    }



    @Transactional
    public ResponseDTO<PatientCreateDTO> changeStatus(int idPatient) {

        ResponseDTO<PatientCreateDTO> resultDTO = new ResponseDTO<>();

        try {
            Patient patient = patientRepository.findById(idPatient)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            patient.setStatus(!patient.isStatus());
            patientRepository.save(patient);

            PatientCreateDTO dto = new PatientCreateDTO();
            dto.setId(patient.getId());
            dto.setStatus(patient.isStatus());

            resultDTO.setMessage("Paciente actualizado exitosamente");
            resultDTO.setSuccess(true);

        } catch (RuntimeException e) {
            resultDTO.setMessage(e.getMessage());
            resultDTO.setSuccess(false);
            resultDTO.setObject(null);
        }

        return resultDTO;
    }


    public ResponseDTO<List<PatientProjection>> getAllPatients() {
        try{
            List<PatientProjection> list = patientRepository.findAllProjectedBy();

            ResponseDTO<List<PatientProjection>> resultDTO = new ResponseDTO<>();
            resultDTO.setObject(list);
            resultDTO.setSuccess(true);
            resultDTO.setMessage(list.size() + " pacientes encontrados");
            return resultDTO;
        }catch (Exception e) {
            ResponseDTO<List<PatientProjection>> response = new ResponseDTO<>();
            response.setMessage("Error al obtener pacientes: " + e.getMessage());
            response.setSuccess(false);
            response.setObject(null);
            return response;
        }
    }



}
