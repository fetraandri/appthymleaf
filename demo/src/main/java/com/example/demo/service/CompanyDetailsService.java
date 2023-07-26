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
        // Récupérer les informations de l'entreprise à partir du repository
        // Utilisez l'ID 1 car nous avons créé une seule entrée pour les informations de l'entreprise dans la table.
        return companyDetailsRepository.findById(1L).orElse(null);
    }
}

