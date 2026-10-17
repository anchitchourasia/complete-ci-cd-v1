package com.heg.dto;


import java.util.List;

public class PassRegistryRequestDTO {
	private String vehicleNo;
    private String vehicleType;
    private String brandModel;
    private Long employeeNo;
    private Long passNo;
    private String empType;
    private String contractorCode;
    private String gateNo;
    private String parkingToBeUsed;
    private String status;
    private String remark;
    private String enterBy;
    private String mobileNo;
    
    

    public Long getPassNo() {
		return passNo;
	}

	public void setPassNo(Long passNo) {
		this.passNo = passNo;
	}

	public String getEnterBy() {
		return enterBy;
	}

	public void setEnterBy(String enterBy) {
		this.enterBy = enterBy;
	}
	public String getMobileNo() {
	    return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
	    this.mobileNo = mobileNo;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private List<DocumentRequestDTO> documents;

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

	public String getBrandModel() {
		return brandModel;
	}

	public void setBrandModel(String brandModel) {
		this.brandModel = brandModel;
	}


	public Long getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(Long employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getContractorCode() {
		return contractorCode;
	}

	public void setContractorCode(String contractorCode) {
		this.contractorCode = contractorCode;
	}

	public String getGateNo() {
		return gateNo;
	}

	public void setGateNo(String gateNo) {
		this.gateNo = gateNo;
	}

	public String getParkingToBeUsed() {
		return parkingToBeUsed;
	}

	public void setParkingToBeUsed(String parkingToBeUsed) {
		this.parkingToBeUsed = parkingToBeUsed;
	}

	public List<DocumentRequestDTO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentRequestDTO> documents) {
		this.documents = documents;
	}
    
    
}
