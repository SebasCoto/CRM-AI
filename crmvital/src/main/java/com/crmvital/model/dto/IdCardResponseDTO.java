package com.crmvital.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class IdCardResponseDTO {
    private List<IdCardDTO> results;
    private String nombre;
    private String cedula;
}

