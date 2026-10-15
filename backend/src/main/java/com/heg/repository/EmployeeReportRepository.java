package com.heg.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.heg.dto.EmployeeReportDTO;

@Repository
public class EmployeeReportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

   
   
    public EmployeeReportDTO findById(Long id) {

        String sql = """
                SELECT
                    ID,
                    NAME,
                    DEPT_CODE,
                    DEPT_NAME,
                    CONTRACTOR_CODE,
                    CONTRACTOR_NAME,
                    AADHAAR_NO,
                    EMP_TYPE
                FROM attd.VPMS_EMPLOYEE
                WHERE ID = ?
                """;

        List<EmployeeReportDTO> list = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(EmployeeReportDTO.class),
                id);

        return list.isEmpty() ? null : list.get(0);
    }
    
    
    
    public List<EmployeeReportDTO> findAll() {

        String sql = """
                SELECT
                    ID,
                    NAME,
                    DEPT_CODE,
                    DEPT_NAME,
                    CONTRACTOR_CODE,
                    CONTRACTOR_NAME,
                    AADHAAR_NO,
                    EMP_TYPE
                FROM attd.VPMS_EMPLOYEE
                """;

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(EmployeeReportDTO.class)
        );
    }
    
}