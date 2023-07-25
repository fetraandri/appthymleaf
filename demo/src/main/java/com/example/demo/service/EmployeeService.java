package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }


    public List<Employee> getAllEmployeesWithFilter(String nom, String prenoms, String sexe, String fonction,
                                                    LocalDate dateEmbaucheStart, LocalDate dateEmbaucheEnd,
                                                    LocalDate dateDepartStart, LocalDate dateDepartEnd) {

        // Filtrer les valeurs de nom, prenoms, sexe et fonction


        List<Employee> employees;

        // Vérifier si au moins un champ de filtrage est rempli
        if (nom != null || prenoms != null || sexe != null || fonction != null ||
                dateEmbaucheStart != null || dateEmbaucheEnd != null ||
                dateDepartStart != null || dateDepartEnd != null) {

            // Appliquer le filtre
            employees = employeeRepository.findAll();

            if (nom != null) {
                employees = employees.stream().filter(employee -> employee.getNom().contains(nom)).collect(Collectors.toList());
            }

            if (prenoms != null) {
                employees = employees.stream().filter(employee -> employee.getPrenoms().contains(prenoms)).collect(Collectors.toList());
            }

            if (sexe != null) {
                employees = employees.stream().filter(employee -> employee.getSexe().contains(sexe)).collect(Collectors.toList());
            }

            if (dateEmbaucheStart != null || dateEmbaucheEnd != null) {
                employees = employees.stream().filter(employee -> {
                    LocalDate dateEmbauche = employee.getDateEmbauche();
                    return (dateEmbauche == null || (dateEmbaucheStart == null || dateEmbauche.isEqual(dateEmbaucheStart) || dateEmbauche.isAfter(dateEmbaucheStart)) &&
                            (dateEmbaucheEnd == null || dateEmbauche.isEqual(dateEmbaucheEnd) || dateEmbauche.isBefore(dateEmbaucheEnd)));
                }).collect(Collectors.toList());
            }

            if (dateDepartStart != null || dateDepartEnd != null) {
                employees = employees.stream().filter(employee -> {
                    LocalDate dateDepart = employee.getDateDepart();
                    return (dateDepart == null || (dateDepartStart == null || dateDepart.isEqual(dateDepartStart) || dateDepart.isAfter(dateDepartStart)) &&
                            (dateDepartEnd == null || dateDepart.isEqual(dateDepartEnd) || dateDepart.isBefore(dateDepartEnd)));
                }).collect(Collectors.toList());
            }
               } else {
            // Sinon, récupérer tous les employés de la base de données
            employees = employeeRepository.findAll();
        }

        return employees;
    }
}






