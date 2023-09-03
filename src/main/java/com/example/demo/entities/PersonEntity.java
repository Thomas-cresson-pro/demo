package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(name = "firstname")
    private String firstName;
    @Setter
    @Column(name = "lastname")
    private String lastName;
    @Setter
    @Column(name = "email")
    private String email;

}
