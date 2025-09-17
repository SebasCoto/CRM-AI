package com.crmvital.repository.professional;

import com.crmvital.model.entity.professional.Professional;
import com.crmvital.projection.ProfessionalProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalRepo extends JpaRepository<Professional, Integer> {

    List<ProfessionalProjection> findAllProjectedBy();
    Professional findByEmail(String email);
    boolean existsByIdCard(String idCard);
    boolean existsByEmail(String email);
    boolean existsByNameProfessionalAndFirstLastNameAndSecondLastName(
            String nameProfessional,
            String firstLastName,
            String secondLastName
    );


}
