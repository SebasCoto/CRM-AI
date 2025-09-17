package com.crmvital.model.dto.assisDTO;

import com.fasterxml.jackson.core.JsonPointer;
import lombok.Data;

@Data
public class AssisDTO {


    private int idAssis;

    private  int idUser;


    private String nameAssistant;

    private String firstLastName;

    private String secondLastName;


    private String email;

    private String phoneNumber;

    private String idCard;



}
