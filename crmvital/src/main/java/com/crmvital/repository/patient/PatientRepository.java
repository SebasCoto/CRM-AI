package com.crmvital.repository.patient;

import com.crmvital.model.entity.patient.Patient;
import com.crmvital.projection.AssistantProjection;
import com.crmvital.projection.PatientProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    List<PatientProjection> findAllProjectedBy();

    boolean existsByIdCard(String idCard);
    boolean existsByEmail(String email);
    boolean existsByNameAndFirstLastNameAndSecondLastName(String name,
                             String firstLastname,
                             String SecondLastName);
}
