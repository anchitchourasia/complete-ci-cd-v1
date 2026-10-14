package com.heg.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table(name = "VPMS_DOCUMENTS")
public class Document {

    // 💡 FIXED FOR ORACLE 10g: Reverted from IDENTITY back to SEQUENCE strategy.
    // Explicitly set allocationSize = 1 to map to your database sequence increment step.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doc_seq")
    @SequenceGenerator(
        name = "doc_seq", 
        sequenceName = "documents_seq", 
        allocationSize = 1
    )
    
    @Column(name = "ID")
    private Long Id;

    @Column(name = "DOCUMENT_TYPE", nullable = false, length = 30)
    private String documentType;
    
    @Column(name = "DOCUMENT_NO", length = 50)
    private String documentNo;
    
   
    @Column(name = "EXPIRY_DATE", nullable = false)
    private LocalDate expiryDate;
    
    @Column(name = "FILE_NAME", length = 150)
    private String fileName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PASS_ID", nullable = false)
    private PassRegistry passRegistry;


	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	
	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public PassRegistry getPassRegistry() {
		return passRegistry;
	}

	public void setPassRegistry(PassRegistry passRegistry) {
		this.passRegistry = passRegistry;
	}
    
    
    
    
    
}










