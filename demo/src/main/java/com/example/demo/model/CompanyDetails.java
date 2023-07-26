package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Entity
@Getter
@Setter

public class CompanyDetails {
    @Id
    private Long id;
    private String companyName;
    private String description;
    private String slogan;
    private String address;
    private String contactEmail;
    private List<String> phoneNumbers;
    private String nif;
    private String stat;
    private String rcs;
    private byte[] logo;
}
