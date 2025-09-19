package com.crmvital.model.entity.patient;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patient")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "first_last_name")
    private String firstLastName;

    @Column(name = "second_last_name")
    private String secondLastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birthdate")
    private LocalDateTime birthDate;

    @Column(name = "sex")
    private String sex;

    @Column(name = "address")
    private String address;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "date_last_modified")
    private LocalDateTime dateLastModified;

    @Column(name = "id_card")
    private String idCard;

    @Column(name = "status")
    private  boolean status;


}
