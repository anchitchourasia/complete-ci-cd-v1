package com.heg.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.heg.dto.AuthorityDTO;

@Repository
public class  AuthorityRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	 public List<AuthorityDTO> findByEmpCode(String empCode) {

	        String sql = """
	            SELECT AUTHORITY_TYPE,
	                   COMPANY_CODE,
	                   DEPARTMENT_CODE,
	                   EMP_CODE,
	                   VALID_FROM,
	                   VALID_TILL
	            FROM VPMS_AUTHORITY
	            WHERE EMP_CODE = ?
	            """;

	        return jdbcTemplate.query(sql, new Object[]{empCode}, (rs, rowNum) -> {

	            AuthorityDTO dto = new AuthorityDTO();
	            dto.setAuthorityType(rs.getString("AUTHORITY_TYPE"));
	            dto.setCompanyCode(rs.getString("COMPANY_CODE"));
	            dto.setDepartmentCode(rs.getString("DEPARTMENT_CODE"));
	            dto.setEmpCode(rs.getString("EMP_CODE"));
	            dto.setValidFrom(rs.getDate("VALID_FROM"));
	            dto.setValidTill(rs.getDate("VALID_TILL"));

	            return dto;
	        });
	    }
}