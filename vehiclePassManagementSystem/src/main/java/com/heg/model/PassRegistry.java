package com.heg.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "VPMS_PASS_REGISTRY",
    schema = "CW",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_PASSNO_EMPTYPE",
            columnNames = {"EMP_TYPE", "PASS_NO"}
        )
    }
)
public class PassRegistry {

	 @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pass_reg_seq_gen")
	    @SequenceGenerator(
	            name = "pass_reg_seq_gen",
	            sequenceName = "PASS_REGISTRY_SEQ",
	            allocationSize = 1)
	    @Column(name = "ID", length = 6)
	    private Long id;

	    @Column(name = "VEHICLE_NO", nullable = false, length = 50)
	    private String vehicleNo;

	    @Column(name = "VEHICLE_TYPE", nullable = false, length = 50)
	    private String vehicleType;

	    @Column(name = "BRAND_MODEL", length = 100)
	    private String brandModel;

	    @Column(name = "EMPLOYEE_NO" , length = 6)
	    private Long employeeNo;

	    @Column(name = "EMP_TYPE", nullable = false, length = 20)
	    private String empType;

	    @Column(name = "CONTRACTOR_CODE", length = 20)
	    private String contractorCode;

	    @Column(name = "GATE_NO", length = 20)
	    private String gateNo;

	    @Column(name = "PARKING_TO_BE_USED", length = 50)
	    private String parkingToBeUsed;

	    @Column(name = "STATUS", length = 20)
	    private String reqStatus;

	    @Column(name = "ENTER_BY", length = 20)
	    private String enterBy;

	    @Column(name = "ENTER_DATE")
	    private LocalDate enterDate;

	    @Column(name = "PASS_NO", nullable = false)
	    private Long passNo;
	    

	    public Long getPassNo() {
			return passNo;
		}

		public void setPassNo(Long passNo) {
			this.passNo = passNo;
		}

		@OneToMany(
	            mappedBy = "passRegistry",
	            cascade = CascadeType.ALL,
	            orphanRemoval = true,
	            fetch = FetchType.LAZY)
	    private List<Document> documents = new ArrayList<>();

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

		public List<Document> getDocuments() {
			return documents;
		}

		public void setDocuments(List<Document> documents) {
			this.documents = documents;
		}

		public String getReqStatus() {
			return reqStatus;
		}

		public void setReqStatus(String reqStatus) {
			this.reqStatus = reqStatus;
		}
	       
		
		


}














