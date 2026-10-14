package com.heg.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heg.dto.EmployeeReportDTO;
import com.heg.service.EmployeeDetailsService;

@RestController
@RequestMapping("/api/reports") // Same base URL style (/api) but isolated to a reports domain
public class EmployeeReportController {

    @Autowired    
    private EmployeeDetailsService empService;

    // Secure API Key token string matching your project standard
    private static final String API_KEY = "VPMS_SECRET_KEY_2026";

   

   
    @GetMapping("/employee-department/{id}")
    public ResponseEntity<?> getEmployeeById(
            @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @PathVariable Long id) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing API Key.");
        }

        EmployeeReportDTO employee = empService.fetchEmployeeById(id);

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee not found.");
        }

        return ResponseEntity.ok(employee);
    }
    
    

    @GetMapping("/employee-department")
    public ResponseEntity<?> getEmployee(
            @RequestHeader(value = "x-api-key", required = false) String apiKey
            ) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing API Key.");
        }

        List<EmployeeReportDTO> employee = empService.fetchEmployee();

        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee not found.");
        }

        return ResponseEntity.ok(employee);
    }
}