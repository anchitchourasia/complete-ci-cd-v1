package com.heg.repository;


import java.sql.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.heg.dto.PassExpiryResponseDTO;

@Repository
public class PassExpiryRepository {

    private final JdbcTemplate jdbcTemplate;

    public PassExpiryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL = """
        SELECT
            a.EMPLOYEE_NO,
            b.NAME,
            b.DEPT_CODE,
            b.DEPT_NAME,
            b.CONTRACTOR_CODE,
            b.CONTRACTOR_NAME,
            b.AADHAAR_NO,
            b.EMP_TYPE,
            b.MOBILE_NO,
            d.rc_expiry_date,
            d.insurance_expiry_date,
            d.license_expiry_date
        FROM VPMS_PASS_REGISTRY a
        JOIN attd.vpms_employee b
            ON b.ID = a.EMPLOYEE_NO
        LEFT JOIN (
            SELECT
                PASS_ID,
                MAX(CASE
                    WHEN DOCUMENT_TYPE = 'RC'
                    THEN EXPIRY_DATE
                END) AS rc_expiry_date,
                MAX(CASE
                    WHEN DOCUMENT_TYPE = 'INSURANCE'
                    THEN EXPIRY_DATE
                END) AS insurance_expiry_date,
                MAX(CASE
                    WHEN DOCUMENT_TYPE = 'LICENSE'
                    THEN EXPIRY_DATE
                END) AS license_expiry_date
            FROM VPMS_DOCUMENTS
            WHERE DOCUMENT_TYPE IN ('RC', 'INSURANCE', 'LICENSE')
            GROUP BY PASS_ID
        ) d
            ON d.PASS_ID = a.ID
        """;

    public List<PassExpiryResponseDTO> getPassExpiryDetails() {

        return jdbcTemplate.query(SQL, (rs, rowNum) -> {

            Date rcDate = rs.getDate("rc_expiry_date");
            Date insuranceDate = rs.getDate("insurance_expiry_date");
            Date licenseDate = rs.getDate("license_expiry_date");

            return new PassExpiryResponseDTO(
                    rs.getLong("EMPLOYEE_NO"),
                    rs.getString("NAME"),
                    rs.getString("DEPT_CODE"),
                    rs.getString("DEPT_NAME"),
                    rs.getString("CONTRACTOR_CODE"),
                    rs.getString("CONTRACTOR_NAME"),
                    rs.getString("AADHAAR_NO"),
                    rs.getString("EMP_TYPE"),
                    rs.getString("MOBILE_NO"),
                    rcDate != null ? rcDate.toLocalDate() : null,
                    insuranceDate != null ? insuranceDate.toLocalDate() : null,
                    licenseDate != null ? licenseDate.toLocalDate() : null
            );
        });
    }
}