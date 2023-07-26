package com.example.demo.controller;

import com.example.demo.model.CompanyDetails;
import com.example.demo.service.CompanyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {

    private final CompanyDetailsService companyDetailsService;

    @Autowired
    public MyController(CompanyDetailsService companyDetailsService) {
        this.companyDetailsService = companyDetailsService;
    }

    @GetMapping("/home")
    public ModelAndView home() {
        // Retrieve the company details
        CompanyDetails companyDetails = companyDetailsService.getCompanyDetails();

        // Create a ModelAndView object and add the company details to it
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("companyDetails", companyDetails);

        return modelAndView;
    }
}




