package com.example.demo.model;

import jakarta.persistence.Entity;
import lombok.*;
import org.thymeleaf.expression.Dates;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
public class Employee {
    private String firstName;
    private String lastName;
    private Dates dateOfBirth;

}

