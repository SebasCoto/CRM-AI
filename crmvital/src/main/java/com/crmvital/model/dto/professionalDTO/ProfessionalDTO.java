package com.crmvital.model.dto.professionalDTO;


import lombok.Data;

@Data
public class ProfessionalDTO {
    private int id;
    private int idUser;
    private String nameProfessional;
    private String firstLastName;
    private String secondLastName;
    private String specialty;
    private String email;
    private String phoneNumber;
    private String idCard;


}
