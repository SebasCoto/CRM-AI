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
import com.crmvital.repository.user.RefreshTokenRepo;
import com.crmvital.repository.user.UserRepository;
import com.crmvital.service.EmailService;
import com.crmvital.util.JwtUtil;
import com.crmvital.util.UsersUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
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
import java.util.Calendar;
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
    private final RefreshTokenRepo refreshTokenRepo;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                       RefreshTokenService refreshTokenService,
                       UserRepository userRepository,
                       ProfessionalRepo professionalRepo,
                       AssistantRepo assistantRepo,
                       PasswordEncoder passwordEncoder,
                       UsersUtil passwordUtil, EmailService emailService,
                       RefreshTokenRepo refreshTokenRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.professionalRepo = professionalRepo;
        this.assistantRepo = assistantRepo;
        this.passwordEncoder = passwordEncoder;
        this.passwordUtil = passwordUtil;
        this.emailService = emailService;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @Transactional
    public ResponseDTO<UserDto> login(UserDto userDto, boolean forceLogin) {
        ResponseDTO<UserDto> response = new ResponseDTO<>();

        try {
            // Autenticación
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );

            User user = userRepository.findByUsername(userDto.getUsername());

            if (!user.isStatus()) {
                response.setSuccess(false);
                response.setMessage("Usuario desactivado");
                return response;
            }

            // Verificar sesión activa
            boolean hasActiveToken = refreshTokenRepo.existsByUserIdAndExpiryDateAfter(user.getId(), new Date());

            if (hasActiveToken && !forceLogin) {
                response.setSuccess(false);
                response.setMessage("Ya existe una sesión activa. ¿Desea continuar?");
                response.setObject(null);
                return response;
            }

            if (hasActiveToken && forceLogin) {
                refreshTokenRepo.deleteByUserId(user.getId());
            }

            String accessToken = jwtUtil.generateToken(userDto);

            RefreshToken refreshToken = refreshTokenService.getRefreshToken(user.getId());

            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setRol_name(user.getRol().getRolName());
            dto.setToken(accessToken);
            dto.setRefreshToken(refreshToken.getToken());
            dto.setPassword(null);
            dto.setEmail(null);

            response.setSuccess(true);
            response.setObject(dto);
            response.setMessage(hasActiveToken ? "Login exitoso. Sesión previa cerrada." : "Login exitoso");
            return response;

        } catch (BadCredentialsException e) {
            response.setSuccess(false);
            response.setMessage("Usuario o contraseña incorrecta");
            return response;
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error al procesar la solicitud");
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
