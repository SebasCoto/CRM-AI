package com.crmvital.service.user;

import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.model.entity.professional.Professional;
import com.crmvital.model.entity.rol.Rol;
import com.crmvital.model.entity.user.User;
import com.crmvital.repository.professional.ProfessionalRepo;
import com.crmvital.repository.rol.RolRepository;
import com.crmvital.repository.user.UserRepository;
import com.crmvital.service.EmailService;
import com.crmvital.util.UsersUtil;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersUtil passWordUtil;
    private final ProfessionalRepo professionalRepo;
    private final EmailService emailService;
    public UserService(UserRepository userRepository,
                       RolRepository rolRepository,
                       PasswordEncoder passwordEncoder,
                       UsersUtil passWordUtil,
                       ProfessionalRepo professionalRepo,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.passWordUtil = passWordUtil;
        this.professionalRepo = professionalRepo;
        this.emailService = emailService;
    }

    public User createUser(int idEntidad, Rol rol) throws MessagingException {

        Professional profesional = professionalRepo.findById(idEntidad)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));


        String username = UsersUtil.generateUsername(profesional.getNameProfessional(),
                profesional.getFirstLastName());


        if(userRepository.existsByUsername(username)) {
            throw new RuntimeException("El usuario ya existe");
        }


        String passwordTemporal = passWordUtil.CreatePassword(10);


        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(passwordTemporal));
        user.setRol(rol);
        user.setStatus(true);

        userRepository.save(user);


        emailService.sendEmail(profesional.getEmail(), "Correo pass", profesional.getNameProfessional(), passwordTemporal);


        return user;
    }


    @Transactional
    public UserDto toggleUserStatus(int idUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setStatus(!user.isStatus());
        userRepository.save(user);

        // Crear DTO solo con status y mensaje
        UserDto dto = new UserDto();
        dto.setStatus(user.isStatus());
        dto.setUsername(user.getUsername());
        dto.setToken(user.isStatus() ? "Usuario activado correctamente" : "Usuario inactivado correctamente");

        return dto;
    }




}
