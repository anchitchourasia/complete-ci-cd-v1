package com.heg.dto;

import java.util.Date;

public class PassHistoryResponseDTO {
	private Long id;

    private Long passNo;

    private Date dateOfEntry;

    private String empCode;

    private String action;

    private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPassNo() {
		return passNo;
	}

	public void setPassNo(Long passNo) {
		this.passNo = passNo;
	}

	public Date getDateOfEntry() {
		return dateOfEntry;
	}

	public void setDateOfEntry(Date dateOfEntry) {
		this.dateOfEntry = dateOfEntry;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    

}
