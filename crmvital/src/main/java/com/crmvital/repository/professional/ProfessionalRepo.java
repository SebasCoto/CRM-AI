package com.crmvital.repository.professional;

import com.crmvital.model.entity.professional.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalRepo extends JpaRepository<Professional, Integer> {

    boolean existsByIdCard(String idCard);
    boolean existsByEmail(String email);
    boolean existsByNameProfessionalAndFirstLastNameAndSecondLastName(
            String nameProfessional,
            String firstLastName,
            String secondLastName
    );


}
