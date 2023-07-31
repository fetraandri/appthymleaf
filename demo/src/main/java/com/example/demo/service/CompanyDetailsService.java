package com.example.demo.service;

import com.example.demo.model.CompanyDetails;
import com.example.demo.repository.CompanyDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyDetailsService {

    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;

    public CompanyDetails getCompanyDetails() {
        return companyDetailsRepository.findById(1L).orElse(null);
    }

    public void saveCompanyDetails(CompanyDetails companyDetails) {
        // Vous pouvez ajouter des validations ici si nécessaire avant de sauvegarder les informations
        // Pour cet exemple, nous allons simplement sauvegarder les informations sans validation
        companyDetails.setId(1L); // Assurez-vous que l'ID est fixe pour la seule entrée d'entreprise
        companyDetailsRepository.save(companyDetails);
    }
}

