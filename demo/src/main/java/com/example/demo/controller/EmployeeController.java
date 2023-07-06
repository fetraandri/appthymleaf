package com.example.demo.controller;

import ch.qos.logback.core.model.Model;
import com.example.demo.model.Employee;
import org.hibernate.mapping.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {
    @GetMapping("/employees")
    public String getEmployees(ModelMap model) {
        List employeeList = // Récupérez la liste d'employés depuis une source de données (par exemple, une base de données)
      return "employee-list"; // Nom du template Thymeleaf pour afficher la liste d'employés
    }
}


