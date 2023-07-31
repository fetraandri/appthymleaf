        package com.example.demo.model;

        import jakarta.persistence.*;
        import lombok.*;

        import java.time.LocalDate;
        import java.util.List;

        @AllArgsConstructor
        @NoArgsConstructor
        @Entity
        @Getter
        @Setter
        @ToString

        public class Employee {

                @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                private Long id;

                private String nom;
                private String prenoms;
                private String sexe;
                private LocalDate dateNaissance;
                private String image;
                private String adresse;
                private String emailPerso;
                private String emailPro;
                private String cinNumero;

                @Column(columnDefinition = "date")
                private LocalDate cinDateDelivrance;

                private String cinLieuDelivrance;
                private String fonction;
                private int nombreEnfants;
                private LocalDate dateEmbauche;
                private LocalDate dateDepart;
                private String categorieSocioProfessionnelle;
                private String numeroCnaps;

                @Column(columnDefinition = "text")
                private String imageFileName; // Nom du fichier de l'image

                @Lob
                private byte[] imageData;

                private List<String> telephones;



        }
