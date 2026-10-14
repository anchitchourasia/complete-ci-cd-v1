package com.heg.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "VPMS_HISTORY")

public class PassHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "pass_history_seq")
    @SequenceGenerator(
            name = "pass_history_seq",
            sequenceName = "PASS_HISTORY_SEQ",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PASS_NO", nullable = false, length = 20)
    private Long passNo;

    @Column(name = "DATE_OF_ENTRY", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date dateOfEntry;

    @Column(name = "EMP_CODE", nullable = false, length = 20)
    private String empCode;

    @Column(name = "ACTION", nullable = false, length = 20)
    private String action;

    @Column(name = "REMARK", length = 200)
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