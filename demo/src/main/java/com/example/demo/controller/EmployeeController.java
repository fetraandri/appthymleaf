package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String addEmployee(@ModelAttribute("newEmployee") Employee employee) {
        employeeService.addEmployee(employee);
        return "redirect:/employees";
    }







}
