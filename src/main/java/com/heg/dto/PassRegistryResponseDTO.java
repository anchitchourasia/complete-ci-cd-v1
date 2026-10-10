package com.heg.dto;

import java.time.LocalDate;
import java.util.List;

public class PassRegistryResponseDTO {
	
	
	 private Long id;

	    private String vehicleNo;

	    private String vehicleType;

	    private String brandModel;	   

	    private Long employeeNo;

	    private String empType;

	    private String contractorCode;

	    private String gateNo;

	    private String parkingToBeUsed;

	    private String enterBy;

	    private LocalDate enterDate;
	    
	    private String reqStatus;
	    
	    private Long passNo;
	    
	    private List<DocumentResponseDTO> documents;
	    
	    

	    public Long getPassNo() {
			return passNo;
		}

		public void setPassNo(Long passNo) {
			this.passNo = passNo;
		}

		public String getReqStatus() {
			return reqStatus;
		}

		public void setReqStatus(String reqStatus) {
			this.reqStatus = reqStatus;
		}

		

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
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

		public String getEnterBy() {
			return enterBy;
		}

		public void setEnterBy(String enterBy) {
			this.enterBy = enterBy;
		}

		public LocalDate getEnterDate() {
			return enterDate;
		}

		public void setEnterDate(LocalDate enterDate) {
			this.enterDate = enterDate;
		}

		public List<DocumentResponseDTO> getDocuments() {
			return documents;
		}

		public void setDocuments(List<DocumentResponseDTO> documents) {
			this.documents = documents;
		}
	    
	    
	    
	    
    
}
