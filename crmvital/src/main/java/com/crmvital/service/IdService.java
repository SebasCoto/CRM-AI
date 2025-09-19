package com.crmvital.service;

import com.crmvital.model.dto.IdCardDTO;
import com.crmvital.model.dto.IdCardResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IdService {
    private final RestTemplate restTemplate;

    public IdService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public IdCardResponseDTO consultId(String id) {
        try {
            String url = "https://apis.gometa.org/cedulas/" + id;
            return restTemplate.getForObject(url, IdCardResponseDTO.class);
        } catch (Exception e) {
            System.err.println("Error al consultar la API: " + e.getMessage());
            return null;
        }
    }


}
