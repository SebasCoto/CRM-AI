package com.crmvital.repository.assistant;

import com.crmvital.model.entity.assistant.Assistant;
import com.crmvital.model.entity.professional.Professional;
import com.crmvital.projection.AssistantProjection;
import com.crmvital.projection.ProfessionalProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssistantRepo extends JpaRepository<Assistant, Integer> {
    Assistant findByEmail(String email);
    List<AssistantProjection> findAllProjectedBy();
    boolean existsByIdCard(String idCard);
    boolean existsByEmail(String email);
    boolean existsByNameAssistantAndFirstLastNameAndSecondLastName(
            String nameProfessional,
            String firstLastName,
            String secondLastName
    );
}
