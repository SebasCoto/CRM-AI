package com.crmvital.service.user;

import com.crmvital.model.dto.ChangePassword;
import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.model.entity.professional.Professional;
import com.crmvital.model.entity.rol.Rol;
import com.crmvital.model.entity.user.User;
import com.crmvital.repository.professional.ProfessionalRepo;
import com.crmvital.repository.rol.RolRepository;
import com.crmvital.repository.user.UserRepository;
import com.crmvital.service.EmailService;
import com.crmvital.util.JwtUtil;
import com.crmvital.util.UsersUtil;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersUtil passWordUtil;
    private final ProfessionalRepo professionalRepo;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    public UserService(UserRepository userRepository,
                       RolRepository rolRepository,
                       PasswordEncoder passwordEncoder,
                       UsersUtil passWordUtil,
                       ProfessionalRepo professionalRepo,
                       EmailService emailService,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.passWordUtil = passWordUtil;
        this.professionalRepo = professionalRepo;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    public User createUser(int idEntidad, Rol rol) throws MessagingException {

        Professional profesional = professionalRepo.findById(idEntidad)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));


        String username = UsersUtil.generateUsername(profesional.getNameProfessional(),
                profesional.getFirstLastName());


        if(userRepository.existsByUsername(username)) {
            throw new RuntimeException("El usuario ya existe");
        }


        String tempPassword = passWordUtil.CreatePassword(10);


        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setRol(rol);
        user.setStatus(true);

        userRepository.save(user);


        emailService.sendEmail(profesional.getEmail(), "Contraseña temporal por registro", profesional.getNameProfessional(), tempPassword);


        return user;
    }


    @Transactional
    public UserDto toggleUserStatus(int idUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setStatus(!user.isStatus());
        userRepository.save(user);


        UserDto dto = new UserDto();
        dto.setStatus(user.isStatus());
        dto.setUsername(user.getUsername());
        dto.setToken(user.isStatus() ? "Usuario activado correctamente" : "Usuario inactivado correctamente");

        return dto;
    }



    public ResponseDTO<UserDto> changePassword(ChangePassword changePassword, String token) {
        ResponseDTO<UserDto> response = new ResponseDTO<>();

        try {
            int idUser = jwtUtil.getIdFromToken(token);
            User user = userRepository.findById(idUser)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


            if (!passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
                response.setSuccess(false);
                response.setMessage("La contraseña anterior no coincide");
                return response;
            }

            if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
                response.setSuccess(false);
                response.setMessage("Las contraseñas nuevas no coinciden");
                return response;
            }

            if (passwordEncoder.matches(changePassword.getNewPassword(), user.getPassword())) {
                response.setSuccess(false);
                response.setMessage("La contraseña nueva no puede ser la misma que la anterior");
                return response;
            }

            user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            user.setIsTempPass(false);
            user.setDateExpirationTempPass(null);

            userRepository.save(user);

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setStatus(user.isStatus());

            response.setSuccess(true);
            response.setMessage("Contraseña cambiada exitosamente");
            response.setObject(userDto);

            return response;

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error al cambiar la contraseña: " + e.getMessage());
            return response;
        }
    }

}
