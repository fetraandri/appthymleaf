package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Controller
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String getAllEmployees(Map<String, Object> model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.put("employees", employees);
        return "employees";
    }


    @GetMapping("/addEmployee")
    public String showAddEmployeeForm(Map<String, Object> model) {
        model.put("newEmployee", new Employee());
        return "addEmployee";
    }


    @PostMapping("/addEmployee")
    public String addEmployee(
            @ModelAttribute("newEmployee") Employee employee,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {


        // Gérer le fichier image envoyé par l'utilisateur
        if (!imageFile.isEmpty()) {
            try {
                // Récupérer le nom du fichier d'image et le stocker dans l'objet Employee
                String fileName = imageFile.getOriginalFilename();
                employee.setImageFileName(fileName);

                // Convertir les données binaires de l'image en byte[]
                byte[] imageData = imageFile.getBytes();

                // Sauvegarder les données binaires de l'image dans l'objet Employee
                employee.setImageData(imageData);
            } catch (IOException e) {
                // Gérer l'exception en cas d'erreur lors de la sauvegarde de l'image
                e.printStackTrace();
                // Vous pouvez également retourner un message d'erreur à l'utilisateur ici
            }
        }

        // Sauvegarder l'employé dans la base de données
        employeeService.addEmployee(employee);
        return "redirect:/employees";
    }



    @GetMapping("/employee/{id}")
    public String getEmployeeDetails(@PathVariable Long id, Map<String, Object> model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.put("employee", employee);
        return "employeeDetails";
    }

    @GetMapping("/employeeImage/{id}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null && employee.getImageData() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Changez en MediaType.IMAGE_JPEG si nécessaire
            return new ResponseEntity<>(employee.getImageData(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // ...

    @PostMapping("/updateEmployee")
    public String updateEmployee(@ModelAttribute("employee") Employee updatedEmployee,
                                 @RequestParam("imageFile") MultipartFile imageFile) {
        Employee existingEmployee = employeeService.getEmployeeById(updatedEmployee.getId());
        if (existingEmployee == null) {
            // Gérer le cas où l'employé n'existe pas
            return "redirect:/employees";
        }

        // Mettre à jour les attributs de l'employé existant
        existingEmployee.setNom(updatedEmployee.getNom());
        existingEmployee.setPrenoms(updatedEmployee.getPrenoms());
        existingEmployee.setDateNaissance(updatedEmployee.getDateNaissance());

        // Gérer le fichier image envoyé par l'utilisateur s'il y en a un
        if (!imageFile.isEmpty()) {
            try {
                // Récupérer le nom du fichier d'image et le stocker dans l'objet Employee
                String fileName = imageFile.getOriginalFilename();
                existingEmployee.setImageFileName(fileName);

                // Convertir les données binaires de l'image en byte[]
                byte[] imageData = imageFile.getBytes();

                // Sauvegarder les données binaires de l'image dans l'objet Employee
                existingEmployee.setImageData(imageData);
            } catch (IOException e) {
                // Gérer l'exception en cas d'erreur lors de la sauvegarde de l'image
                e.printStackTrace();
                // Vous pouvez également retourner un message d'erreur à l'utilisateur ici
            }
        }

        // Mettre à jour l'employé dans la base de données
        employeeService.updateEmployee(existingEmployee);
        return "redirect:/employee/" + existingEmployee.getId();
    }









}