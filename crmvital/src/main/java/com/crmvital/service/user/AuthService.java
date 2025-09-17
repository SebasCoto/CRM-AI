package com.crmvital.service.user;

import com.crmvital.model.RefreshToken;
import com.crmvital.model.dto.ResponseDTO;
import com.crmvital.model.dto.loginDTO.LoginRequest;
import com.crmvital.model.dto.loginDTO.LoginResponse;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.model.entity.assistant.Assistant;
import com.crmvital.model.entity.professional.Professional;
import com.crmvital.model.entity.user.User;
import com.crmvital.repository.assistant.AssistantRepo;
import com.crmvital.repository.professional.ProfessionalRepo;
import com.crmvital.repository.user.UserRepository;
import com.crmvital.service.EmailService;
import com.crmvital.util.JwtUtil;
import com.crmvital.util.UsersUtil;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final ProfessionalRepo professionalRepo;
    private final AssistantRepo assistantRepo;
    private final PasswordEncoder passwordEncoder;
    private final UsersUtil passwordUtil;
    private final EmailService emailService;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                       RefreshTokenService refreshTokenService,
                       UserRepository userRepository,
                       ProfessionalRepo professionalRepo,
                       AssistantRepo assistantRepo,
                       PasswordEncoder passwordEncoder,
                       UsersUtil passwordUtil, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.professionalRepo = professionalRepo;
        this.assistantRepo = assistantRepo;
        this.passwordEncoder = passwordEncoder;
        this.passwordUtil = passwordUtil;
        this.emailService = emailService;
    }

    public ResponseDTO<UserDto> login(UserDto userDto) {
        ResponseDTO<UserDto> response = new ResponseDTO<>();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getUsername(),
                            userDto.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername());

            if (!user.isStatus()) {
                response.setSuccess(false);
                response.setMessage("Usuario desactivado");
                return response;
            }


            LocalDateTime expiration = user.getDateExpirationTempPass();
            LocalDateTime now = LocalDateTime.now();

            if (expiration != null && now.isAfter(expiration)) {
                response.setSuccess(false);
                response.setMessage("La contraseña temporal ya expiró");
                return response;
            }




            UserDto userDtoForToken = new UserDto();
            userDtoForToken.setId(user.getId());
            userDtoForToken.setUsername(user.getUsername());
            userDtoForToken.setRol_name(user.getRol().getRolName());

            String token = jwtUtil.generateToken(userDtoForToken);
            RefreshToken refreshToken = refreshTokenService.getRefreshToken(user.getId());

            userDtoForToken.setPassword(null);
            userDtoForToken.setToken(token);

            userDtoForToken.setPassword(null);
            userDtoForToken.setEmail(null);
            userDtoForToken.setOldPassword(null);
            userDtoForToken.setNewPassword(null);
            userDtoForToken.setConfirmPassword(null);

            response.setSuccess(true);
            response.setMessage("Login exitoso");
            response.setObject(userDtoForToken);
            return response;

        } catch (BadCredentialsException e) {
            response.setSuccess(false);
            response.setMessage("Usuario o contraseña incorrecta");
            return response;
        }catch (Exception e) {
            log.error("Error al autenticar", e);
            response.setSuccess(false);
            response.setMessage("Error al procesar la solicitud, favor comunicarse con el proveedor");
            return response;
        }

    }



    public ResponseDTO<UserDto> resetPassword(String email) {
        ResponseDTO<UserDto> response = new ResponseDTO<>();

        try {
            Professional pro = professionalRepo.findByEmail(email);
            Assistant as = assistantRepo.findByEmail(email);

            if (pro == null && as == null) {
                response.setSuccess(false);
                response.setMessage("Usuario no encontrado, verifique el correo");
                return response;
            }

            int userId = pro != null ? pro.getUser().getId() : as.getUser().getId();
            String nombre = pro != null ? pro.getNameProfessional() : as.getNameAssistant();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


            if (!user.isStatus()) {
                response.setSuccess(false);
                response.setMessage("Usuario desactivado");
                return response;
            }

            String tempPassword = passwordUtil.CreatePassword(10);
            user.setPassword(passwordEncoder.encode(tempPassword));
            user.setIsTempPass(true);
            user.setDateExpirationTempPass(LocalDateTime.now().plusHours(2));
            userRepository.save(user);

            try {
                emailService.sendEmail(email, "Restablecimiento de contraseña", nombre, tempPassword);
            } catch (MessagingException e) {
                response.setSuccess(false);
                response.setMessage("Error al enviar correo: " + e.getMessage());
                return response;
            }

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setStatus(user.isStatus());

            response.setSuccess(true);
            response.setMessage("Password reset exitoso");
            response.setObject(userDto);
            return response;

        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage("Error durante el reset de contraseña: " + ex.getMessage());
            return response;
        }
    }













}
