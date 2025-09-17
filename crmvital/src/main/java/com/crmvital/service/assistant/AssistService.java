package com.crmvital.service.assistant;

import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.assisDTO.AssisDTO;
import com.crmvital.model.dto.professionalDTO.ProfessionalDTO;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.model.entity.assistant.Assistant;
import com.crmvital.model.entity.rol.Rol;
import com.crmvital.model.entity.user.User;
import com.crmvital.projection.AssistantProjection;
import com.crmvital.projection.ProfessionalProjection;
import com.crmvital.repository.assistant.AssistantRepo;
import com.crmvital.repository.rol.RolRepository;
import com.crmvital.repository.user.UserRepository;
import com.crmvital.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AssistService {

    private final UserService userService;
    private AssistantRepo assistantRepo;
    private UserRepository  userRepository;
    private RolRepository rolRepository;

    public AssistService (AssistantRepo assistantRepo,
                          UserRepository userRepository,
                          RolRepository rolRepository, UserService userService) {
        this.assistantRepo = assistantRepo;
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.userService = userService;
    }



    @Transactional
    public ResponseDTO<AssisDTO> insertAssistant(AssisDTO assisDTO) {
        ResponseDTO<AssisDTO> response = new ResponseDTO<>();

        try {
            validateAssistant(assisDTO);

            Assistant assistant = getAssistant(assisDTO);
            assistantRepo.save(assistant);

            Rol rolAssistant = rolRepository.findByRolName("ROLE_ASSISTANT")
                    .orElseThrow(() -> new RuntimeException("Rol asistente no encontrado"));

            User createdUser = userService.createUser(assistant.getId(), rolAssistant);

            assistant.setUser(createdUser);
            assistantRepo.save(assistant);

            AssisDTO resultDTO = new AssisDTO();
            resultDTO.setNameAssistant(assistant.getNameAssistant());
            resultDTO.setFirstLastName(assistant.getFirstLastName());
            resultDTO.setSecondLastName(assistant.getSecondLastName());
            resultDTO.setEmail(assistant.getEmail());
            resultDTO.setPhoneNumber(assistant.getPhoneNumber());
            resultDTO.setIdUser(createdUser.getId());

            response.setSuccess(true);
            response.setMessage("Asistente creado correctamente");
            response.setObject(resultDTO);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error al crear asistente: " + e.getMessage());
            response.setObject(null);
        }

        return response;
    }

    private void validateAssistant(AssisDTO dto) {
        if (dto.getIdCard() == null || dto.getIdCard().length() < 9) {
            throw new IllegalArgumentException("La cédula no tiene el número de caracteres correcto");
        }

        if (dto.getIdCard() == null || dto.getIdCard().length() > 9) {
            throw new IllegalArgumentException("La cédula no tiene el número de caracteres correcto");
        }

        if (assistantRepo.existsByIdCard(dto.getIdCard())) {
            throw new IllegalArgumentException("La cédula ya existe en la base de datos");
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }

        if (assistantRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (assistantRepo.existsByNameAssistantAndFirstLastNameAndSecondLastName(
                dto.getNameAssistant(),
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


        if (dto.getNameAssistant() == null || dto.getNameAssistant().isBlank()) {
            throw new IllegalArgumentException("El nombre del profesional es obligatorio");
        }
        if (dto.getFirstLastName() == null || dto.getFirstLastName().isBlank()) {
            throw new IllegalArgumentException("El primer apellido es obligatorio");
        }
    }

    private Assistant getAssistant(AssisDTO assisDTO) {
        Assistant  assistant = new Assistant();
        assistant.setNameAssistant(assisDTO.getNameAssistant());
        assistant.setFirstLastName(assisDTO.getFirstLastName());
        assistant.setSecondLastName(assisDTO.getSecondLastName());
        assistant.setEmail(assisDTO.getEmail());
        assistant.setPhoneNumber(assisDTO.getPhoneNumber());
        assistant.setIdCard(assisDTO.getIdCard());
        return assistant;
    }

    @Transactional
    public ResponseDTO<AssisDTO> updateAssistant(AssisDTO assisDTO) {
        ResponseDTO<AssisDTO> responseDTO = new ResponseDTO<>();


        Assistant assistant = assistantRepo.findById(assisDTO.getIdAssis())
                .orElseThrow(() -> new RuntimeException("Asistente no encontrado con ID: " + assisDTO.getIdAssis()));

        boolean changed = false;

        if (!Objects.equals(assistant.getNameAssistant(), assisDTO.getNameAssistant())) {
            assistant.setNameAssistant(assisDTO.getNameAssistant());
            changed = true;
        }
        if (!Objects.equals(assistant.getFirstLastName(), assisDTO.getFirstLastName())) {
            assistant.setFirstLastName(assisDTO.getFirstLastName());
            changed = true;
        }
        if (!Objects.equals(assistant.getSecondLastName(), assisDTO.getSecondLastName())) {
            assistant.setSecondLastName(assisDTO.getSecondLastName());
            changed = true;
        }
        if (!Objects.equals(assistant.getEmail(), assisDTO.getEmail())) {
            assistant.setEmail(assisDTO.getEmail());
            changed = true;
        }
        if (!Objects.equals(assistant.getPhoneNumber(), assisDTO.getPhoneNumber())) {
            assistant.setPhoneNumber(assisDTO.getPhoneNumber());
            changed = true;
        }
        if (!Objects.equals(assistant.getIdCard(), assisDTO.getIdCard())) {
            assistant.setIdCard(assisDTO.getIdCard());
            changed = true;
        }

        if (changed) {
            assistantRepo.save(assistant);
        }

        AssisDTO resultDTO = new AssisDTO();
        resultDTO.setIdAssis(assistant.getId());
        resultDTO.setNameAssistant(assistant.getNameAssistant());
        resultDTO.setFirstLastName(assistant.getFirstLastName());
        resultDTO.setSecondLastName(assistant.getSecondLastName());
        resultDTO.setEmail(assistant.getEmail());
        resultDTO.setPhoneNumber(assistant.getPhoneNumber());
        resultDTO.setIdCard(assistant.getIdCard());

        responseDTO.setSuccess(true);
        responseDTO.setObject(resultDTO);
        responseDTO.setMessage(changed ? "Asistente actualizado correctamente" : "No hubo cambios");

        return responseDTO;
    }

    @Transactional
    public ResponseDTO<UserDto> toggleAssistantUserStatus(int idAssis) {
        ResponseDTO<UserDto> responseDTO = new ResponseDTO<>();

        try {
            Assistant assistant = assistantRepo.findById(idAssis)
                    .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

            UserDto updatedUser = userService.toggleUserStatus(assistant.getUser().getId());

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


    public ResponseDTO<List<AssistantProjection>> getAllAssistants() {
        try {
            List<AssistantProjection> list = assistantRepo.findAllProjectedBy();

            ResponseDTO<List<AssistantProjection>> response = new ResponseDTO<>();
            response.setMessage(list.size() + " assistants encontrados");
            response.setSuccess(true);
            response.setObject(list);
            return response;
        } catch (Exception e) {
            ResponseDTO<List<AssistantProjection>> response = new ResponseDTO<>();
            response.setMessage("Error al obtener asistentes: " + e.getMessage());
            response.setSuccess(false);
            response.setObject(null);
            return response;
        }    }
}
