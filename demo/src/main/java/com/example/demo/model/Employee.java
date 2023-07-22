package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString

public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id ;

        private String nom;
        private String prenoms;
        private LocalDate dateNaissance;
        private String image ;

        @Column(columnDefinition = "text")
        private String imageFileName; // Nom du fichier de l'image

        @Lob
        private byte[] imageData;



}

