package com.heg.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heg.dto.EmployeeReportDTO;
import com.heg.repository.EmployeeReportRepository;

@Service
public  class EmployeeDetailsService{

    @Autowired
    private EmployeeReportRepository empRepo;

   
    public EmployeeReportDTO fetchEmployeeById(Long id) {
        return empRepo.findById(id);
    }
    
    public List<EmployeeReportDTO> fetchEmployee() {
        return empRepo.findAll();
    }

}
