package com.example.demo.controller;

import com.example.demo.model.CompanyDetails;
import com.example.demo.service.CompanyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class MyController {

    private final CompanyDetailsService companyDetailsService;

    @Autowired
    public MyController(CompanyDetailsService companyDetailsService) {
        this.companyDetailsService = companyDetailsService;
    }

    // Endpoint pour enregistrer les informations de l'entreprise
    @PostMapping("/saveCompanyDetails")
    public String saveCompanyDetails(@ModelAttribute CompanyDetails companyDetails, @RequestParam("logoFile") MultipartFile logoFile) {
        if (!logoFile.isEmpty()) {
            try {
                // Récupérer le nom du fichier d'image et le stocker dans l'objet CompanyDetails
                String fileName = logoFile.getOriginalFilename();
                companyDetails.setLogoFileName(fileName);

                // Convertir les données binaires de l'image en byte[]
                byte[] logoData = logoFile.getBytes();

                // Sauvegarder les données binaires de l'image dans l'objet CompanyDetails
                companyDetails.setLogoData(logoData);
            } catch (IOException e) {
                // Gérer l'exception en cas d'erreur lors de la sauvegarde de l'image
                e.printStackTrace();
                // Vous pouvez également retourner un message d'erreur à l'utilisateur ici
            }
        }

        companyDetailsService.saveCompanyDetails(companyDetails);
        return "redirect:/home";
    }



    @GetMapping("/home")
    public ModelAndView home() {
        // Récupérer les informations de l'entreprise
        CompanyDetails companyDetails = companyDetailsService.getCompanyDetails();

        // Si aucune entreprise enregistrée, créer une nouvelle instance
        if (companyDetails == null) {
            companyDetails = new CompanyDetails();
        }

        // Créer une ModelAndView et ajouter les détails de l'entreprise pour l'affichage
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("companyDetails", companyDetails);

        return modelAndView;
    }
    @GetMapping("/companyDetailsLogo/{id}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable Long id) {
        CompanyDetails companyDetails = companyDetailsService.getCompanyDetails();
        if (companyDetails != null && companyDetails.getLogoData() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Changez en MediaType.IMAGE_JPEG si nécessaire
            return new ResponseEntity<>(companyDetails.getLogoData(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/addCompany")
    public ModelAndView addCompany() {
        // Créer une nouvelle instance de CompanyDetails
        CompanyDetails companyDetails = new CompanyDetails();

        // Créer une ModelAndView et ajouter l'objet CompanyDetails pour le formulaire
        ModelAndView modelAndView = new ModelAndView("addCompany");
        modelAndView.addObject("companyDetails", companyDetails);

        return modelAndView;
    }

}






