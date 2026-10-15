package com.heg.dto;

public class PassRegistryListDTO {
	private Long id;
    private String passNo;
    private String vehicleNo;
    private String vehicleType;
    private String empType;
    private String employeeNo;
    private String status;

    private String name;
    private String deptCode;
    private String deptName;
    private String contractorCode;
    private String contractorName;
    private String aadhaarNo;

    public PassRegistryListDTO() {
    }

    public PassRegistryListDTO(Long id, String passNo, String vehicleNo,
                           String vehicleType, String empType,
                           String employeeNo, String status,
                           String name, String deptCode,
                           String deptName, String contractorCode,
                           String contractorName, String aadhaarNo) {

        this.id = id;
        this.passNo = passNo;
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
        this.empType = empType;
        this.employeeNo = employeeNo;
        this.status = status;
        this.name = name;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.contractorCode = contractorCode;
        this.contractorName = contractorName;
        this.aadhaarNo = aadhaarNo;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassNo() {
		return passNo;
	}

	public void setPassNo(String passNo) {
		this.passNo = passNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
    
    
}
