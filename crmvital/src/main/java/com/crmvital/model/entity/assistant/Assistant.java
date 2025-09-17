package com.crmvital.model.entity.assistant;


import jakarta.persistence.*;
import lombok.Data;
import com.crmvital.model.entity.user.User;

@Entity
@Data
@Table(name = "assistant")
public class Assistant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_assistant")
    private int id;

    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;

    @Column(name="first_name")
    private String nameAssistant;

    @Column(name = "first_last_name")
    private String firstLastName;

    @Column(name = "second_last_name")
    private String secondLastName;


    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "id_card")
    private String idCard; //Cedula







}
