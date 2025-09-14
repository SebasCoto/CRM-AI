package com.crmvital.service.user;

import com.crmvital.config.security.WebSecurityConfig;
import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.model.entity.rol.Rol;
import com.crmvital.model.entity.user.User;
import com.crmvital.repository.rol.RolRepository;
import com.crmvital.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RolRepository rolRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto insertUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        Rol rol = rolRepository.findById(userDto.getId_rol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRol(rol);
        user.setStatus(true);

        userRepository.save(user);

        return new UserDto(user.getUsername(), null, user.getRol().getId());
    }
}
