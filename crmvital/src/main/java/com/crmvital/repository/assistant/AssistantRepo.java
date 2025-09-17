package com.crmvital.repository.assistant;

import com.crmvital.model.entity.assistant.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistantRepo extends JpaRepository<Assistant, Integer> {
    Assistant findByEmail(String email);
}
