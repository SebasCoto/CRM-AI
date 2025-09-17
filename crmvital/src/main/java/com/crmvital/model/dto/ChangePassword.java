package com.crmvital.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePassword {
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    private String newPassword;

    @NotBlank
    private String confirmPassword;

}
