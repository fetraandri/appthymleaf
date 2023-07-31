package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Controller
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/filtered-employees")
    public String getFilteredEmployees(HttpSession session, Map<String, Object> model) {
        // Récupérer les employés filtrés depuis la session
        List<Employee> filteredEmployees = (List<Employee>) session.getAttribute("filteredEmployees");

        if (filteredEmployees == null) {
            // Aucun employé filtré trouvé, rediriger ou gérer le cas où il n'y a pas d'employés filtrés.
            // Par exemple, rediriger vers une autre page.
            return "redirect:/employees";
        }

        // Utiliser les employés filtrés dans le modèle
        model.put("filteredEmployees", filteredEmployees);
        return "filtered-employees";
    }

    @GetMapping("/employees")
    public String getAllEmployees(@RequestParam(required = false) String nom,
                                  @RequestParam(required = false) String prenoms,
                                  @RequestParam(required = false) String sexe,
                                  @RequestParam(required = false) String fonction,
                                  @RequestParam(required = false) LocalDate dateEmbaucheStart,
                                  @RequestParam(required = false) LocalDate dateEmbaucheEnd,
                                  @RequestParam(required = false) LocalDate dateDepartStart,
                                  @RequestParam(required = false) LocalDate dateDepartEnd,
                                  @RequestParam(required = false, defaultValue = "nom") String sortBy,
                                  @RequestParam(required = false, defaultValue = "asc") String sortOrder,
                                  Map<String, Object> model,
                                  HttpSession session) {

        List<Employee> employees;

        // Vérifier si au moins un champ de filtrage est rempli
        // ...
// Vérifier si au moins un champ de filtrage ou de tri est rempli
        if (nom != null || prenoms != null || sexe != null || fonction != null ||
                dateEmbaucheStart != null || dateEmbaucheEnd != null ||
                dateDepartStart != null || dateDepartEnd != null || !sortBy.equals("nom")) {
            // Appliquer le filtre et le tri


            employees = employeeService.getAllEmployeesWithFilterAndSort(nom, prenoms, sexe, fonction,
                    dateEmbaucheStart, dateEmbaucheEnd,
                    dateDepartStart, dateDepartEnd,
                    sortBy, sortOrder);

            session.setAttribute("filteredEmployees", employees);

        } else {
            // Sinon, récupérer tous les employés de la base de données sans filtrage ni tri
            employees = employeeService.getAllEmployees();
        }
// ...


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
        existingEmployee.setSexe(updatedEmployee.getSexe());
        existingEmployee.setDateNaissance(updatedEmployee.getDateNaissance());
        existingEmployee.setAdresse(updatedEmployee.getAdresse());
        existingEmployee.setEmailPerso(updatedEmployee.getEmailPerso());
        existingEmployee.setEmailPro(updatedEmployee.getEmailPro());
        existingEmployee.setCinNumero(updatedEmployee.getCinNumero());
        existingEmployee.setCinDateDelivrance(updatedEmployee.getCinDateDelivrance());
        existingEmployee.setCinLieuDelivrance(updatedEmployee.getCinLieuDelivrance());
        existingEmployee.setFonction(updatedEmployee.getFonction());
        existingEmployee.setNombreEnfants(updatedEmployee.getNombreEnfants());
        existingEmployee.setDateEmbauche(updatedEmployee.getDateEmbauche());
        existingEmployee.setDateDepart(updatedEmployee.getDateDepart());
        existingEmployee.setCategorieSocioProfessionnelle(updatedEmployee.getCategorieSocioProfessionnelle());
        existingEmployee.setNumeroCnaps(updatedEmployee.getNumeroCnaps());
        existingEmployee.setTelephones(updatedEmployee.getTelephones());


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





    @GetMapping("/employees/export/csv")
    public void exportFilteredEmployeesToCSV(HttpSession session, HttpServletResponse response) {
        // Récupérer les employés filtrés depuis la session
        List<Employee> filteredEmployees = (List<Employee>) session.getAttribute("filteredEmployees");

        if (filteredEmployees == null || filteredEmployees.isEmpty()) {
            // Aucun employé filtré trouvé, rediriger ou gérer le cas où il n'y a pas d'employés filtrés.
            // Par exemple, rediriger vers une autre page.
            return;
        }

        // Nom du fichier CSV et en-têtes des colonnes
        String fileName = "filtered_employees.csv";
        String[] headers = {"ID", "Nom", "Prénoms", "Sexe", "Date de naissance", "Image", "Adresse", "Email Perso",
                "Email Pro", "CIN Numero", "CIN Date Delivrance", "CIN Lieu Delivrance", "Fonction",
                "Nombre d'enfants", "Date d'embauche", "Date de départ", "Catégorie socio-professionnelle",
                "Numéro CNAPS", "Nom du fichier de l'image", "Image Data", "Téléphones"};

        try {
            // Définir le type de contenu de la réponse comme étant un fichier CSV
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // Créer un écrivain CSV en utilisant la bibliothèque Apache Commons CSV
            CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(), CSVFormat.DEFAULT.withHeader(headers));

            // Écrire les données des employés filtrés dans le fichier CSV
            for (Employee employee : filteredEmployees) {
                csvPrinter.printRecord(
                        employee.getId(),
                        employee.getNom(),
                        employee.getPrenoms(),
                        employee.getSexe(),
                        employee.getDateNaissance(),
                        employee.getImage(),
                        employee.getAdresse(),
                        employee.getEmailPerso(),
                        employee.getEmailPro(),
                        employee.getCinNumero(),
                        employee.getCinDateDelivrance(),
                        employee.getCinLieuDelivrance(),
                        employee.getFonction(),
                        employee.getNombreEnfants(),
                        employee.getDateEmbauche(),
                        employee.getDateDepart(),
                        employee.getCategorieSocioProfessionnelle(),
                        employee.getNumeroCnaps(),
                        employee.getImageFileName(),
                        employee.getImageData(),
                        employee.getTelephones()
                );
            }


            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs d'exportation CSV si nécessaire
        }
    }

}
