package com.crmvital.model.dto.patientDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientCreateDTO {

    private int id;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private String address;
    private String email;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String idCard;
    private boolean status;

}
