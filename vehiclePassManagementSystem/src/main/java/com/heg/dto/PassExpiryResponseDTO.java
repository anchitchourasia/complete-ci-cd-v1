package com.heg.dto;

import java.time.LocalDate;

public class PassExpiryResponseDTO {
	private Long employeeNo;
    private String name;
    private String deptCode;
    private String deptName;
    private String contractorCode;
    private String contractorName;
    private String aadhaarNo;
    private String empType;
    private String mobileNo;

    private LocalDate rcExpiryDate;
    private LocalDate insuranceExpiryDate;
    private LocalDate licenseExpiryDate;

    public PassExpiryResponseDTO() {
    }

    public PassExpiryResponseDTO(
            Long employeeNo,
            String name,
            String deptCode,
            String deptName,
            String contractorCode,
            String contractorName,
            String aadhaarNo,
            String empType,
            String mobileNo,
            LocalDate rcExpiryDate,
            LocalDate insuranceExpiryDate,
            LocalDate licenseExpiryDate) {

        this.employeeNo = employeeNo;
        this.name = name;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.contractorCode = contractorCode;
        this.contractorName = contractorName;
        this.aadhaarNo = aadhaarNo;
        this.empType = empType;
        this.mobileNo = mobileNo;
        this.rcExpiryDate = rcExpiryDate;
        this.insuranceExpiryDate = insuranceExpiryDate;
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public Long getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(Long employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(String contractorCode) {
        this.contractorCode = contractorCode;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public LocalDate getRcExpiryDate() {
        return rcExpiryDate;
    }

    public void setRcExpiryDate(LocalDate rcExpiryDate) {
        this.rcExpiryDate = rcExpiryDate;
    }

    public LocalDate getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(LocalDate insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public LocalDate getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }
}
