package com.crmvital.service.professional;

import com.crmvital.model.dto.IdCardDTO;
import com.crmvital.model.dto.IdCardResponseDTO;
import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.patientDTO.PatientCreateDTO;
import com.crmvital.model.dto.professionalDTO.ProfessionalDTO;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.model.entity.professional.Professional;
import com.crmvital.model.entity.rol.Rol;
import com.crmvital.model.entity.user.User;
import com.crmvital.projection.ProfessionalProjection;
import com.crmvital.repository.professional.ProfessionalRepo;
import com.crmvital.repository.rol.RolRepository;
import com.crmvital.service.IdService;
import com.crmvital.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

import static com.crmvital.util.UsersUtil.capitalizeWords;

@Service
public class ProfessionalService {

    private final ProfessionalRepo professionalRepo;
    private final UserService userService;
    private final RolRepository rolRepository;
    private final IdService idService;

    public ProfessionalService(ProfessionalRepo professionalRepo,
                               UserService userService,
                               RolRepository rolRepository,
                               IdService idService) {
        this.professionalRepo = professionalRepo;
        this.userService = userService;
        this.rolRepository = rolRepository;
        this.idService = idService;
    }


    @Transactional
    public ResponseDTO<ProfessionalDTO> insertProfessional(ProfessionalDTO professionalDTO) throws MessagingException {

        try{


            Professional professional = getProfessional(professionalDTO);
            professionalDTO.setNameProfessional(professional.getNameProfessional());
            professionalDTO.setFirstLastName(professional.getFirstLastName());
            professionalDTO.setSecondLastName(professional.getSecondLastName());
            validateProfessional(professionalDTO);

            professionalRepo.save(professional);


            Rol rolProfesional = rolRepository.findByRolName("ROLE_PROFESSIONAL")
                    .orElseThrow(() -> new RuntimeException("Rol Profesional no encontrado"));


            User createdUser = userService.createUser(professional.getId(), rolProfesional);

            professional.setUser(createdUser);
            professionalRepo.save(professional);

           ResponseDTO<ProfessionalDTO> responseDTO = new ResponseDTO<>();
           responseDTO.setSuccess(true);
           responseDTO.setMessage("Se ha registrado el paciente correctamente");
           responseDTO.setObject(professionalDTO);

           return responseDTO;
        }catch (IllegalArgumentException ex) {
            ResponseDTO<ProfessionalDTO> responseDTO = new ResponseDTO<>();
            responseDTO.setSuccess(false);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setObject(null);
            return responseDTO;

        } catch (DataAccessException ex) {
            ResponseDTO<ProfessionalDTO> responseDTO = new ResponseDTO<>();
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error al registrar el paciente: " + ex.getMessage());
            responseDTO.setObject(null);
            return responseDTO;
        }


    }

    private Professional getProfessional(ProfessionalDTO professionalDTO) {
        Professional  professional = new Professional();
        boolean obtainedName = false;

        IdCardResponseDTO response = idService.consultId(professionalDTO.getIdCard());

        if(response != null && response.getResults() != null && !response.getResults().isEmpty()) {
            IdCardDTO result = response.getResults().get(0);

            professional.setNameProfessional(capitalizeWords(result.getFirstname1()));
            professional.setFirstLastName(capitalizeWords(result.getLastname1()));
            professional.setSecondLastName(capitalizeWords(result.getLastname2()));
            obtainedName = true;
        } else if(professionalDTO.getNameProfessional() != null
                    && professionalDTO.getFirstLastName() != null) {
            professional.setNameProfessional(professionalDTO.getNameProfessional());
            professional.setFirstLastName(professionalDTO.getFirstLastName());
            professional.setSecondLastName(professionalDTO.getSecondLastName());
            obtainedName = true;
        }

        if(!obtainedName) {
            throw new IllegalArgumentException(
                    "No se pudo obtener el nombre automáticamente. Por favor ingrese el nombre manualmente."
            );        }

        professional.setSpecialty(professionalDTO.getSpecialty());
        professional.setEmail(professionalDTO.getEmail());
        professional.setPhoneNumber(professionalDTO.getPhoneNumber());
        professional.setIdCard(professionalDTO.getIdCard());
        return professional;





    }

    private void validateProfessional(ProfessionalDTO dto) {
        if (dto.getIdCard() == null || dto.getIdCard().length() < 9) {
            throw new IllegalArgumentException("La cédula no tiene el número de caracteres correcto");
        }

        if (dto.getIdCard() == null || dto.getIdCard().length() > 9) {
            throw new IllegalArgumentException("La cédula no tiene el número de caracteres correcto");
        }

        if (professionalRepo.existsByIdCard(dto.getIdCard())) {
            throw new IllegalArgumentException("La cédula ya existe en la base de datos");
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }

        if (professionalRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (professionalRepo.existsByNameProfessionalAndFirstLastNameAndSecondLastName(
                dto.getNameProfessional(),
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


        if (dto.getNameProfessional() == null || dto.getNameProfessional().isBlank()) {
            throw new IllegalArgumentException("El nombre del profesional es obligatorio");
        }
        if (dto.getFirstLastName() == null || dto.getFirstLastName().isBlank()) {
            throw new IllegalArgumentException("El primer apellido es obligatorio");
        }
        if (dto.getSpecialty() == null || dto.getSpecialty().isBlank()) {
            throw new IllegalArgumentException("La especialidad es obligatoria");
        }
    }

    @Transactional
    public ProfessionalDTO updateProfessional(@RequestBody ProfessionalDTO professionalDTO) {

        Professional professional = professionalRepo.findById(professionalDTO.getId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        boolean changed = false;

        if (!Objects.equals(professional.getNameProfessional(), professionalDTO.getNameProfessional())) {
            professional.setNameProfessional(professionalDTO.getNameProfessional());
            changed = true;
        }
        if (!Objects.equals(professional.getFirstLastName(), professionalDTO.getFirstLastName())) {
            professional.setFirstLastName(professionalDTO.getFirstLastName());
            changed = true;
        }
        if (!Objects.equals(professional.getSecondLastName(), professionalDTO.getSecondLastName())) {
            professional.setSecondLastName(professionalDTO.getSecondLastName());
            changed = true;
        }
        if (!Objects.equals(professional.getSpecialty(), professionalDTO.getSpecialty())) {
            professional.setSpecialty(professionalDTO.getSpecialty());
            changed = true;
        }
        if (!Objects.equals(professional.getEmail(), professionalDTO.getEmail())) {
            professional.setEmail(professionalDTO.getEmail());
            changed = true;
        }
        if (!Objects.equals(professional.getPhoneNumber(), professionalDTO.getPhoneNumber())) {
            professional.setPhoneNumber(professionalDTO.getPhoneNumber());
            changed = true;
        }
        if(!Objects.equals(professional.getIdCard(), professionalDTO.getIdCard())) {
            professional.setIdCard(professionalDTO.getIdCard());
            changed = true;
        }

        if (changed) {
            professionalRepo.save(professional);
        }


        ProfessionalDTO resultDTO = new ProfessionalDTO();
        resultDTO.setId(professional.getId());
        resultDTO.setNameProfessional(professional.getNameProfessional());
        resultDTO.setFirstLastName(professional.getFirstLastName());
        resultDTO.setSecondLastName(professional.getSecondLastName());
        resultDTO.setSpecialty(professional.getSpecialty());
        resultDTO.setEmail(professional.getEmail());
        resultDTO.setPhoneNumber(professional.getPhoneNumber());

        return resultDTO;
    }


    @Transactional
    public ResponseDTO<UserDto> toggleProfessionalUserStatus(int idProfessional) {
        ResponseDTO<UserDto> responseDTO = new ResponseDTO<>();

        try {
            Professional professional = professionalRepo.findById(idProfessional)
                    .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

            UserDto updatedUser = userService.toggleUserStatus(professional.getUser().getId());

            responseDTO.setSuccess(true);
            responseDTO.setObject(updatedUser);
            responseDTO.setMessage("Estado del usuario actualizado correctamente");
            return responseDTO;

        } catch (RuntimeException e) {
            responseDTO.setSuccess(false);
            responseDTO.setObject(null);
            responseDTO.setMessage(e.getMessage());
            return responseDTO;
        }
    }



    public ResponseDTO<List<ProfessionalProjection>> getAllProfessionals() {
        try {
            List<ProfessionalProjection> list = professionalRepo.findAllProjectedBy();

            ResponseDTO<List<ProfessionalProjection>> response = new ResponseDTO<>();
            response.setMessage(list.size() + " professionals encontrados");
            response.setSuccess(true);
            response.setObject(list);
            return response;
        } catch (Exception e) {
            ResponseDTO<List<ProfessionalProjection>> response = new ResponseDTO<>();
            response.setMessage("Error al obtener profesionales: " + e.getMessage());
            response.setSuccess(false);
            response.setObject(null);
            return response;
        }
    }


}
