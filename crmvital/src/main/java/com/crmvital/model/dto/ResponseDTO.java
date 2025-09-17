package com.crmvital.model.dto;

import lombok.Data;

@Data
public class ResponseDTO<T> {

    private String message;
    private Boolean success;
    private T object;


}
